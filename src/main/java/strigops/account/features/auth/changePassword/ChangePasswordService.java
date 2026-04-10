package strigops.account.features.auth.changePassword;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import strigops.account.features.auth.changePassword.dto.ChangePasswordRequest;
import strigops.account.features.identity.repository.UsersRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChangePasswordService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body cannot be null");
        }

        log.info("Initiating password change for user: {}", request.email());

        var user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> {
                    log.warn("Change password failed: User with email {} not found", request.email());
                    return new RuntimeException("User not found");
                });

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            log.warn("Change password failed: Incorrect old password for user {}", request.email());
            throw new RuntimeException("Old password incorrect");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old password");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        eventPublisher.publishEvent(new PasswordChangeEvent(this, user.getEmail()));

        log.info("Password successfully changed for user: {}", user.getEmail());
    }
}