package strigops.account.features.auth.changePassword;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@Slf4j
@RequiredArgsConstructor
public class PasswordChangeListener {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @EventListener
    @Async
    public void handlePasswordChange(PasswordChangeEvent event) {
        try {
            log.info("Preparing HTML email for: {}", event.getEmail());

            Context context = new Context();
            context.setVariable("email", event.getEmail());

            String htmlContent = templateEngine.process("change-password", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(event.getEmail());
            helper.setSubject("Strigops - Password Change Notification");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("HTML Email sent successfully to: {}", event.getEmail());
        }catch (Exception e) {
            log.error("Failed to send email to {}: {}", event.getEmail(), e.getMessage());
        }
    }
}
