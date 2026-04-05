package strigops.account.features.auth.register;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import strigops.account.features.auth.register.command.CreateUserCommand;
import strigops.account.features.auth.register.command.UserRegistrationResult;
import strigops.account.internal.domain.entity.UsersEntity;
import strigops.account.internal.domain.repository.UsersRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

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
                .build();

        var savedUser = usersRepository.save(newUser);
        log.info("User created with id={}", savedUser.getId());
        return new UserRegistrationResult(savedUser.getId(), savedUser.getEmail());
    }
}
