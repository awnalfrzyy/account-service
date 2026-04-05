package strigops.account.features.auth.changePassword;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import strigops.account.features.auth.changePassword.dto.ChangePasswordRequest;
import strigops.account.internal.domain.repository.UsersRepository;

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
            throw new BadRequestException("Request body cannot be null");
        }
        log.info("Initiating password change for user: {}", request.email());

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Change password failed: User with email {} not found", request.email());
                    return new BadRequestException("User not found");
                });

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            log.warn("Change password failed: Incorrect old password for user {}", request.email());
            throw new BadRequestException("Old password incorrect");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new BadRequestException("New password cannot be the same as the old password");
        }

        String hashedNewPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(hashedNewPassword);

        userRepository.save(user);

        eventPublisher.publishEvent(new PasswordChangeEvent(this, user.getEmail()));
    }

}
