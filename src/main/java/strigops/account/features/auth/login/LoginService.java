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
import strigops.account.features.identity.repository.UsersRepository;
import strigops.account.features.session.SessionService;
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
    public void enableUser(String email){
        usersRepository.findByEmail(email)
                .ifPresentOrElse(user -> {
                    user.setActive(true);
                    usersRepository.save(user);
                    log.info("User {} is now active", email);
                }, () -> log.warn("Failed to enable user: {} not found", email));
    }
}