package strigops.account.features.auth.register.users;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import strigops.account.common.validator.annotation.UniqueEmail;
import strigops.account.features.auth.register.users.command.CreateUserCommand;
import strigops.account.features.auth.register.users.command.UserRegistrationResult;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.entity.extension.UserStatus;
import strigops.account.features.identity.repository.UsersRepository;
import strigops.account.features.auth.otp.OtpService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService{

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    @Transactional
    public UserRegistrationResult register(CreateUserCommand command) {

        log.info("Registering new user");
        var newUser = UsersEntity.builder()
                .email(command.email().trim().toLowerCase())
                .password(passwordEncoder.encode(command.password()))
                .username(command.username())
                .lastIp(command.ipAddress())
                .latitude(command.latitude())
                .placeName(command.placeName())
                .status(UserStatus.PENDING)
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
            user.setStatus(UserStatus.ACTIVE);
            usersRepository.save(user);
            log.info("User {} is now active", email);
        });
    }
}

