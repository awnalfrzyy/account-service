package strigops.account.internal.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private OtpService otpService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void shouldGenerateAndStoreOtp() {
        // Given
        String email = "test@example.com";

        // When
        String otp = otpService.generateOtp(email);

        // Then
        assertNotNull(otp, "OTP should not be null");
        assertEquals(6, otp.length(), "OTP should be 6 digits");
        assertTrue(otp.matches("\\d{6}"), "OTP should contain only digits");

        verify(valueOperations).set(eq("otp:" + email.toLowerCase()), anyString(), eq(Duration.ofMinutes(5)));
    }

    @Test
    void shouldSendOtpEmail() {
        // Given
        String email = "test@example.com";
        String otp = "123456";

        // When
        otpService.sendOtpEmail(email, otp);

        // Then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldVerifyValidOtp() {
        // Given
        String email = "test@example.com";
        String otp = "123456";
        when(valueOperations.get("otp:" + email.toLowerCase())).thenReturn(otp);

        // When
        boolean isValid = otpService.verifyOtp(email, otp);

        // Then
        assertTrue(isValid, "Valid OTP should be verified");
        verify(redisTemplate).delete("otp:" + email.toLowerCase());
        verify(valueOperations).set(eq("verified:" + email.toLowerCase()), eq("true"), eq(Duration.ofMinutes(10)));
    }

    @Test
    void shouldRejectInvalidOtp() {
        // Given
        String email = "test@example.com";
        String storedOtp = "123456";
        String inputOtp = "654321";
        when(valueOperations.get("otp:" + email.toLowerCase())).thenReturn(storedOtp);

        // When
        boolean isValid = otpService.verifyOtp(email, inputOtp);

        // Then
        assertFalse(isValid, "Invalid OTP should be rejected");
        verify(redisTemplate, never()).delete(anyString());
        verify(valueOperations, never()).set(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void shouldRejectExpiredOtp() {
        // Given
        String email = "test@example.com";
        String otp = "123456";
        when(valueOperations.get("otp:" + email.toLowerCase())).thenReturn(null);

        // When
        boolean isValid = otpService.verifyOtp(email, otp);

        // Then
        assertFalse(isValid, "Expired OTP should be rejected");
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void shouldMarkEmailAsVerified() {
        // Given
        String email = "test@example.com";

        // When
        otpService.markEmailVerified(email);

        // Then
        verify(valueOperations).set(eq("verified:" + email.toLowerCase()), eq("true"), eq(Duration.ofMinutes(10)));
    }

    @Test
    void shouldCheckEmailVerificationStatus() {
        // Given
        String email = "test@example.com";
        when(valueOperations.get("verified:" + email.toLowerCase())).thenReturn("true");

        // When
        boolean isVerified = otpService.isEmailVerified(email);

        // Then
        assertTrue(isVerified, "Verified email should return true");
    }

    @Test
    void shouldReturnFalseForUnverifiedEmail() {
        // Given
        String email = "test@example.com";
        when(valueOperations.get("verified:" + email.toLowerCase())).thenReturn(null);

        // When
        boolean isVerified = otpService.isEmailVerified(email);

        // Then
        assertFalse(isVerified, "Unverified email should return false");
    }

    @Test
    void shouldNormalizeEmailToLowercase() {
        // Given
        String email = "Test@Example.COM";
        String otp = "123456";
        when(valueOperations.get("otp:test@example.com")).thenReturn(otp);

        // When
        boolean isValid = otpService.verifyOtp(email, otp);

        // Then
        assertTrue(isValid, "Email should be normalized to lowercase");
        verify(valueOperations).get("otp:test@example.com");
    }

    @Test
    void shouldHandleNullEmail() {
        // Given
        String email = null;
        String otp = "123456";

        // When & Then
        assertThrows(NullPointerException.class, () -> otpService.verifyOtp(email, otp),
                "Null email should throw exception");
    }

    @Test
    void shouldHandleNullOtp() {
        // Given
        String email = "test@example.com";
        String otp = null;

        // When
        boolean isValid = otpService.verifyOtp(email, otp);

        // Then
        assertFalse(isValid, "Null OTP should be invalid");
    }
}