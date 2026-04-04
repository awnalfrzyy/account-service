package strigops.account.internal.infrastructure.config;

import java.security.SecureRandom;
import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;
    private final OtpEmailService otpEmailService;

    private static final int OTP_LENGTH = 6;
    private static final Duration OTP_EXPIRY = Duration.ofMinutes(5);

    public String generateOtp(String email) {
        String otp = generateRandomOtp();
        String key = "otp:" + email.toLowerCase();
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY);
        log.info("OTP generated for email: {}", email);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String key = "otp:" + email.toLowerCase();
        String storedOtp = redisTemplate.opsForValue().get(key);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(key);
            markEmailVerified(email);
            log.info("OTP verified successfully for email: {}", email);
            return true;
        }
        log.warn("OTP verification failed for email: {}", email);
        return false;
    }

    public void sendOtpEmail(String email, String otp) {
        try {
            otpEmailService.sendOtpEmail(email, "User", otp, (int) OTP_EXPIRY.toMinutes());
        } catch (Exception e) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp + "\n\nThis code will expire in 5 minutes.");
            mailSender.send(message);
        }
        log.info("OTP email sent to: {}", email);
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
}
