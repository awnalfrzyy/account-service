package strigops.account.internal.infrastructure.config;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "mySecretKeyForJWTTokenGenerationAndValidation12345678901234567890");
        ReflectionTestUtils.setField(jwtService, "expiration", 604800000L); // 7 days in milliseconds

        userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .roles("USER")
                .build();
    }

    @Test
    void shouldGenerateToken() {
        // When
        String token = jwtService.generateToken(userDetails.getUsername());

        // Then
        assertNotNull(token, "Token should not be null");
        assertTrue(token.length() > 0, "Token should not be empty");
    }

    @Test
    void shouldExtractUsernameFromToken() {
        // Given
        String token = jwtService.generateToken(userDetails.getUsername());

        // When
        String extractedUsername = jwtService.extractUsername(token);

        // Then
        assertEquals(userDetails.getUsername(), extractedUsername, "Extracted username should match");
    }

    @Test
    void shouldExtractExpirationDateFromToken() {
        // Given
        String token = jwtService.generateToken(userDetails.getUsername());

        // When
        Date expiration = jwtService.extractExpiration(token);

        // Then
        assertNotNull(expiration, "Expiration date should not be null");
        assertTrue(expiration.after(new Date()), "Expiration should be in the future");
    }

    @Test
    void shouldValidateValidToken() {
        // Given
        String token = jwtService.generateToken(userDetails.getUsername());

        // When
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Then
        assertTrue(isValid, "Valid token should be validated successfully");
    }

    @Test
    void shouldRejectTokenWithWrongUsername() {
        // Given
        String token = jwtService.generateToken(userDetails.getUsername());
        UserDetails wrongUser = User.builder()
                .username("wrong@example.com")
                .password("password")
                .roles("USER")
                .build();

        // When
        boolean isValid = jwtService.validateToken(token, wrongUser);

        // Then
        assertFalse(isValid, "Token with wrong username should be rejected");
    }

    @Test
    void shouldRejectExpiredToken() {
        // Given - Create token with very short expiration
        JwtService shortLivedJwtService = new JwtService();
        ReflectionTestUtils.setField(shortLivedJwtService, "secret", "mySecretKeyForJWTTokenGenerationAndValidation12345678901234567890");
        ReflectionTestUtils.setField(shortLivedJwtService, "expiration", 1L); // 1 millisecond

        String token = shortLivedJwtService.generateToken(userDetails.getUsername());

        // Wait for token to expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When
        boolean isExpired = shortLivedJwtService.isTokenExpired(token);

        // Then
        assertTrue(isExpired, "Expired token should be detected as expired");
    }

    @Test
    void shouldExtractCustomClaims() {
        // Given - Create token with custom claims using reflection
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        claims.put("department", "IT");

        // Use reflection to access private createToken method
        try {
            java.lang.reflect.Method createTokenMethod = JwtService.class.getDeclaredMethod("createToken", Map.class, String.class);
            createTokenMethod.setAccessible(true);
            String token = (String) createTokenMethod.invoke(jwtService, claims, userDetails.getUsername());

            // When
            String role = jwtService.extractClaim(token, claimsObj -> claimsObj.get("role", String.class));
            String department = jwtService.extractClaim(token, claimsObj -> claimsObj.get("department", String.class));

            // Then
            assertEquals("ADMIN", role, "Custom claim 'role' should be extracted correctly");
            assertEquals("IT", department, "Custom claim 'department' should be extracted correctly");
        } catch (Exception e) {
            fail("Failed to test custom claims: " + e.getMessage());
        }
    }

    @Test
    void shouldHandleInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken),
                "Invalid token should throw exception");
    }

    @Test
    void shouldHandleNullToken() {
        // When & Then
        assertThrows(NullPointerException.class, () -> jwtService.extractUsername(null),
                "Null token should throw exception");
    }

    @Test
    void shouldHandleEmptyToken() {
        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractUsername(""),
                "Empty token should throw exception");
    }

    @Test
    void shouldGenerateTokenWithCustomClaims() {
        // Given - Test that generateToken creates a valid token (claims are empty by default)
        String token = jwtService.generateToken(userDetails.getUsername());

        // When
        Claims claims = jwtService.extractClaim(token, claimsObj -> claimsObj);

        // Then
        assertNotNull(claims, "Claims should be extractable from token");
        assertEquals(userDetails.getUsername(), claims.getSubject(), "Subject should match username");
    }
}