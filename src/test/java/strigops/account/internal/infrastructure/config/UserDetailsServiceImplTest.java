package strigops.account.internal.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import strigops.account.internal.domain.entity.UsersEntity;
import strigops.account.internal.domain.repository.UsersRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        String email = "test@example.com";
        String password = "encodedPassword";
        UsersEntity user = UsersEntity.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password(password)
                .active(true)
                .build();

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        var userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals(email, userDetails.getUsername(), "Username should match email");
        assertEquals(password, userDetails.getPassword(), "Password should match");
        assertTrue(userDetails.getAuthorities().isEmpty(), "Authorities should be empty for basic user");

        verify(usersRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email),
                "Should throw UsernameNotFoundException for non-existent user");

        assertTrue(exception.getMessage().contains(email), "Exception message should contain the email");

        verify(usersRepository).findByEmail(email);
    }

    @Test
    void shouldNormalizeEmailToLowercase() {
        // Given
        String inputEmail = "Test@Example.COM";
        String normalizedEmail = "test@example.com";
        String password = "encodedPassword";
        UsersEntity user = UsersEntity.builder()
                .id(UUID.randomUUID())
                .email(normalizedEmail)
                .password(password)
                .active(true)
                .build();

        when(usersRepository.findByEmail(normalizedEmail)).thenReturn(Optional.of(user));

        // When
        var userDetails = userDetailsService.loadUserByUsername(inputEmail);

        // Then
        assertEquals(normalizedEmail, userDetails.getUsername(), "Username should be normalized to lowercase");

        verify(usersRepository).findByEmail(normalizedEmail);
    }

    @Test
    void shouldHandleNullEmail() {
        // Given
        String email = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> userDetailsService.loadUserByUsername(email),
                "Null email should throw exception");
    }

    @Test
    void shouldHandleEmptyEmail() {
        // Given
        String email = "";
        when(usersRepository.findByEmail("")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email),
                "Empty email should throw exception");
    }

    @Test
    void shouldHandleUserWithSpecialCharactersInEmail() {
        // Given
        String email = "test+tag@example.com";
        String password = "encodedPassword";
        UsersEntity user = UsersEntity.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password(password)
                .active(true)
                .build();

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        var userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertEquals(email, userDetails.getUsername(), "Should handle emails with special characters");

        verify(usersRepository).findByEmail(email);
    }

    @Test
    void shouldHandleInactiveUser() {
        // Given
        String email = "inactive@example.com";
        String password = "encodedPassword";
        UsersEntity user = UsersEntity.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password(password)
                .active(false) // User is inactive
                .build();

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        var userDetails = userDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails, "Should still load inactive user (security framework handles activation)");
        assertEquals(email, userDetails.getUsername(), "Username should match");

        verify(usersRepository).findByEmail(email);
    }
}