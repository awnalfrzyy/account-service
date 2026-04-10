package strigops.account.features.auth.recovery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import strigops.account.features.auth.recovery.command.ResetPasswordCommand;
import strigops.account.features.auth.recovery.dto.ForgotPasswordRequest;

import strigops.account.features.identity.entity.PasswordResetToken;
import strigops.account.features.identity.entity.UsersEntity;

import strigops.account.features.identity.repository.UsersRepository;
import strigops.account.features.identity.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void sendRecoveryLink(ForgotPasswordRequest request){
        UsersEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("If the email is registered, instructions will be sent."));

        tokenRepository.deleteByUser(user);
     String token = UUID.randomUUID().toString();
     PasswordResetToken resetToken = PasswordResetToken.builder()
             .token(token)
             .user(user)
             .expiredDate(LocalDateTime.now().plusMinutes(15))
             .used(false)
             .build();

     tokenRepository.save(resetToken);

     eventPublisher.publishEvent(new PasswordResetEvent(user.getEmail(), token));
     log.info("Recovery link generated for user: {}", user.getId());
    }

    @Transactional
    public void resetPassword(ResetPasswordCommand command) {
        PasswordResetToken resetToken = tokenRepository.findByToken(command.token())
                .orElseThrow(() -> new IllegalArgumentException("Token is invalid or not found"));

        if (!resetToken.isValid()) {
            tokenRepository.delete(resetToken);
            throw new IllegalStateException("Token has expired or has already been used");
        }

        UsersEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(command.newPassword()));

        userRepository.save(user);

        tokenRepository.delete(resetToken);

        eventPublisher.publishEvent(new PasswordResetEvent(
                user.getEmail(),
                user.getUsername(),
                LocalDateTime.now()
        ));

        log.info("Password successfully reset for user ID: {}", user.getId());
    }
}
