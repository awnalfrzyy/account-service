package strigops.account.features.auth.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import strigops.account.features.auth.register.command.CreateUserCommand;
import strigops.account.features.auth.register.command.UserRegistrationResult;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.repository.UsersRepository;
import strigops.account.features.auth.otp.OtpService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    @Transactional
    public UserRegistrationResult register(CreateUserCommand command) {
        log.debug("Checking email availability for {}", command.email());
        if (usersRepository.existsByEmail(command.email())) {
            log.warn("Registration failed: email already registered={}", command.email());
            throw new EmailAlreadyRegisteredException();
        }

        log.info("Registering new user");
        var newUser = UsersEntity.builder()
                .email(command.email().trim().toLowerCase())
                .password(passwordEncoder.encode(command.password()))
                .username(command.username())
                .active(false)
                .build();

        var user = usersRepository.save(newUser);

        otpService.sendAndSaveOtp(
                user.getEmail(),
                user.getUsername(),
                "REGISTER"
        );

        return new UserRegistrationResult(
                user.getId(),
                user.getEmail(),
                user.getUsername()
                );
    }

    @Transactional
    public void enableUser(String email){
        usersRepository.findByEmail(email).ifPresent(user -> {
            user.setActive(true);
            usersRepository.save(user);
            log.info("User {} is now active", email);
        });
    }
}
