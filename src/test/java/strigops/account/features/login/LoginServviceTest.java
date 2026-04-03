package strigops.account.features.login;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import strigops.account.features.login.command.LoginCommand;
import strigops.account.features.login.command.LoginResult;
import strigops.account.internal.domain.entity.UsersEntity;
import strigops.account.internal.domain.repository.UsersRepository;
import strigops.account.internal.infrastructure.config.JwtService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServviceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginServvice loginService;

    @Test
    void shouldLoginSuccessfully() {
        // Given
        LoginCommand command = new LoginCommand("test@example.com", "password123");
        UUID userId = UUID.randomUUID();
        UsersEntity user = UsersEntity.builder()
                .id(userId)
                .email("test@example.com")
                .build();
        UserDetails userDetails = mock(UserDetails.class);
        String token = "jwt-token-123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(usersRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken("test@example.com")).thenReturn(token);

        // When
        LoginResult result = loginService.login(command);

        // Then
        assertNotNull(result, "Login result should not be null");
        assertEquals(userId, result.getUserId(), "User ID should match");
        assertEquals("test@example.com", result.getEmail(), "Email should match");
        assertEquals(token, result.getToken(), "Token should match");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("test@example.com");
        verify(usersRepository).findByEmail("test@example.com");
        verify(jwtService).generateToken("test@example.com");
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationFails() {
        // Given
        LoginCommand command = new LoginCommand("test@example.com", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        // When & Then
        assertThrows(RuntimeException.class, () -> loginService.login(command),
                "Should throw exception for bad credentials");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(usersRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        LoginCommand command = new LoginCommand("nonexistent@example.com", "password123");
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("nonexistent@example.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("nonexistent@example.com");
        when(usersRepository.findByEmail("nonexistent@example.com")).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> loginService.login(command),
                "Should throw exception when user not found in repository");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("nonexistent@example.com");
        verify(usersRepository).findByEmail("nonexistent@example.com");
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void shouldNormalizeEmailToLowercase() {
        // Given
        LoginCommand command = new LoginCommand("Test@Example.COM", "password123");
        UUID userId = UUID.randomUUID();
        UsersEntity user = UsersEntity.builder()
                .id(userId)
                .email("test@example.com")
                .build();
        UserDetails userDetails = mock(UserDetails.class);
        String token = "jwt-token-123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(usersRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken("test@example.com")).thenReturn(token);

        // When
        LoginResult result = loginService.login(command);

        // Then
        assertEquals("test@example.com", result.getEmail(), "Email should be normalized to lowercase");

        verify(userDetailsService).loadUserByUsername("test@example.com");
        verify(usersRepository).findByEmail("test@example.com");
    }

    @Test
    void shouldHandleNullCommand() {
        // When & Then
        assertThrows(NullPointerException.class, () -> loginService.login(null),
                "Null command should throw exception");
    }

    @Test
    void shouldHandleNullEmail() {
        // Given
        LoginCommand command = new LoginCommand(null, "password123");

        // When & Then
        assertThrows(NullPointerException.class, () -> loginService.login(command),
                "Null email should throw exception");
    }

    @Test
    void shouldHandleNullPassword() {
        // Given
        LoginCommand command = new LoginCommand("test@example.com", null);

        // When & Then
        assertThrows(NullPointerException.class, () -> loginService.login(command),
                "Null password should throw exception");
    }
}