package strigops.account.features.auth.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strigops.account.features.auth.login.command.LoginCommand;
import strigops.account.features.auth.login.dto.LoginResponse;
import strigops.account.features.auth.otp.OtpService;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.entity.extension.UserStatus;
import strigops.account.features.identity.repository.UsersRepository;
import strigops.account.features.session.SessionService;
import strigops.account.internal.infrastructure.security.jwt.JwtService;
import strigops.shared.exceptions.InvalidCredentialsException;
import strigops.shared.exceptions.UserNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;
    private final OtpService otpService;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse login(LoginCommand command) {
        log.info("Login attempt for email: {}", command.getEmail());

        UsersEntity user = usersRepository.findByEmail(command.getEmail().toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", command.getEmail());
            throw new InvalidCredentialsException();
        }

        otpService.sendAndSaveOtp(user.getEmail(), user.getUsername(), "LOGIN");
        return LoginResponse.requireOtp(user.getEmail());
    }

    @Transactional
    public LoginResponse verifyLogin(
            String email,
            String otpCode,
            String userAgent,
            String ipAddress,
            String purpose
    ) {
        // 1. Verifikasi OTP dulu
        otpService.verifyOtp(email, otpCode, purpose);

        // 2. Ambil User
        UsersEntity user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 3. Update status jika belum aktif (misal dari flow Register.js)
        if (user.getStatus() != UserStatus.ACTIVE) {
            user.setStatus(UserStatus.ACTIVE);
            usersRepository.save(user);
        }

        // 4. Generate Token (Oper userId & role dari entity)
        // Asumsi: user.getId() mengembalikan String/UUID, user.getUsersRole() mengembalikan Enum
        String accessToken = jwtService.generateAccessToken(
                user.getId().toString(),
                user.getRole().name()
        );

        String refreshToken = jwtService.generateRefreshToken(user.getId().toString());

        // 5. Handle Session (Simpan ke DB/Redis lewat sessionService)
        sessionService.handleLogin(user, userAgent, ipAddress, purpose);

        // 6. Return Response
        return LoginResponse.success(user.getId(), user.getEmail(), accessToken, refreshToken);
    }

    @Transactional
    public void enableUser(String email){
        usersRepository.findByEmail(email)
                .ifPresentOrElse(user -> {
                    user.setStatus(UserStatus.ACTIVE);
                    usersRepository.save(user);
                    log.info("User {} is now active", email);
                }, () -> log.warn("Failed to enable user: {} not found", email));
    }
}