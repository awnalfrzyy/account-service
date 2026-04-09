package strigops.account.features.auth.otp;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final TemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    @Value("https://support.strigops.com")
    private String supportUrl ;

    @Value("https://strigops.com/verify")
    private String verifyBaseUrl ;

    private static final int OTP_LENGTH = 6;
    private static final Duration OTP_EXPIRY = Duration.ofMinutes(5);

    public void sendAndSaveOtp(String email, String userName, String purpose) {
        String otp = generateRandomOtp();
        String key = "otp:" + purpose + email.toLowerCase();

        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY);
        log.info("OTP generated and saved for: {}", email);

        try {
            sendOtpEmail(email, userName, otp);
            log.info("OTP email sent to: {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to: {}", email, e);
            throw new RuntimeException("Failed send email verification");
        }
    }

    public boolean verifyOtp(String email, String otp, String purpose) {
        String key = "otp:" + purpose + email.toLowerCase();
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(key);
            redisTemplate.opsForValue().set("verified:" + email.toLowerCase(), "true", Duration.ofMinutes(10));
            return true;
        }
        log.warn("OTP verification failed for email: {}", email);
        return false;
    }

    public void sendOtpEmail(String email, String username, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("Strigops <noreply@strigops.com>");
        helper.setTo(email);
        helper.setSubject("Kode Verifikasi OTP Anda - Strigops Account");

        Map<String, Object> variables = Map.of(
                "name", username,
                "otp", otp,
                "validity_minutes", OTP_EXPIRY.toMinutes(),
                "verify_link", verifyBaseUrl + "?otp=" + otp + "&email=" + email,
                "support_url", supportUrl,
                "privacy_url", "https://strigops.com/privacy",
                "terms_url", "https://strigops.com/terms"
        );

        Context context = new Context();
        context.setVariables(variables);

        String htmlContent;
        try {
            htmlContent = templateEngine.process("otp-email", context);
        } catch (Exception e) {
            log.warn("Thymeleaf template NOT FOUND: {}. Using fallback string.", e.getMessage());
            htmlContent = replaceVariables(getDefaultHtmlTemplate(), variables);
        }

        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    public void markEmailVerified(String email) {
        String key = "verified:" + email.toLowerCase();
        redisTemplate.opsForValue().set(key, "true", Duration.ofMinutes(10));
        log.info("Email marked as verified: {}", email);
    }

    public boolean isEmailVerified(String email) {
        String key = "verified:" + email.toLowerCase();
        String verified = redisTemplate.opsForValue().get(key);
        return "true".equals(verified);
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private String replaceVariables(String template, Map<String, Object> variables) {
        String result = template;
        for (var entry : variables.entrySet()) {
            result = result.replace("[[" + entry.getKey() + "]]", String.valueOf(entry.getValue()));
        }
        return result;
    }


    private String getDefaultHtmlTemplate(){
        return """
                <!DOCTYPE html>
                        <html>
                        <body style="font-family: sans-serif; padding: 20px; color: #333;">
                            <div style="max-width: 500px; margin: auto; border: 1px solid #eee; padding: 20px;">
                                <h2 style="color: #1a73e8;">Strigops Verification</h2>
                                <p>Hello [[name]],</p>
                                <p>Your verification code is:</p>
                                <div style="background: #f4f4f4; padding: 20px; text-align: center; font-size: 30px; font-weight: bold; letter-spacing: 5px;">
                                    [[otp]]
                                </div>
                                <p style="font-size: 12px; color: #666;">This code expires in [[validity_minutes]] minutes.</p>
                                <hr style="border: none; border-top: 1px solid #eee;">
                                <p style="font-size: 11px; color: #999;">If you didn't request this, please ignore this email.</p>
                            </div>
                        </body>
                        </html>
                """;
    }
}
