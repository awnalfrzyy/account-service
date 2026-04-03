package strigops.account.internal.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import strigops.account.internal.domain.entity.SosialAccounts;
import strigops.account.internal.domain.entity.UsersEntity;
import strigops.account.internal.domain.repository.SosialAccountsRepository;
import strigops.account.internal.domain.repository.UsersRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private SosialAccountsRepository sosialAccountsRepository;

    @Mock
    private OAuth2UserRequest userRequest;

    @Mock
    private OAuth2User oauth2User;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Test
    void shouldReturnExistingSocialUser() {
        // Given
        String provider = "google";
        String providerId = "123456789";
        String email = "test@example.com";
        String name = "Test User";
        UUID userId = UUID.randomUUID();

        UsersEntity existingUser = UsersEntity.builder()
                .id(userId)
                .email(email)
                .build();

        SosialAccounts existingSocial = SosialAccounts.builder()
                .user(existingUser)
                .provider(provider)
                .providerUserId(providerId)
                .build();

        when(userRequest.getClientRegistration()).thenReturn(mock(org.springframework.security.oauth2.client.registration.ClientRegistration.class));
        when(userRequest.getClientRegistration().getRegistrationId()).thenReturn(provider);
        when(oauth2User.getAttribute("id")).thenReturn(providerId);
        when(oauth2User.getAttribute("email")).thenReturn(email);
        when(oauth2User.getAttribute("name")).thenReturn(name);
        when(sosialAccountsRepository.findByProviderAndProviderUserId(provider, providerId))
                .thenReturn(Optional.of(existingSocial));

        // Mock the default service behavior
        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
        doReturn(oauth2User).when(spyService).loadUser(userRequest);

        // When
        OAuth2User result = spyService.loadUser(userRequest);

        // Then
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof CustomOAuth2User, "Result should be CustomOAuth2User");
        CustomOAuth2User customUser = (CustomOAuth2User) result;
        assertEquals(userId, customUser.getUserId(), "User ID should match");
        assertEquals(email, customUser.getEmail(), "Email should match");

        verify(sosialAccountsRepository).findByProviderAndProviderUserId(provider, providerId);
        verify(usersRepository, never()).findByEmail(anyString());
        verify(usersRepository, never()).save(any(UsersEntity.class));
        verify(sosialAccountsRepository, never()).save(any(SosialAccounts.class));
    }

    @Test
    void shouldLinkToExistingUserWithSameEmail() {
        // Given
        String provider = "google";
        String providerId = "123456789";
        String email = "existing@example.com";
        String name = "Test User";
        UUID existingUserId = UUID.randomUUID();

        UsersEntity existingUser = UsersEntity.builder()
                .id(existingUserId)
                .email(email)
                .build();

        when(userRequest.getClientRegistration()).thenReturn(mock(org.springframework.security.oauth2.client.registration.ClientRegistration.class));
        when(userRequest.getClientRegistration().getRegistrationId()).thenReturn(provider);
        when(oauth2User.getAttribute("id")).thenReturn(providerId);
        when(oauth2User.getAttribute("email")).thenReturn(email);
        when(oauth2User.getAttribute("name")).thenReturn(name);
        when(oauth2User.getAttribute("picture")).thenReturn("http://example.com/photo.jpg");
        when(sosialAccountsRepository.findByProviderAndProviderUserId(provider, providerId))
                .thenReturn(Optional.empty());
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Mock the default service behavior
        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
        doReturn(oauth2User).when(spyService).loadUser(userRequest);

        // When
        OAuth2User result = spyService.loadUser(userRequest);

        // Then
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof CustomOAuth2User, "Result should be CustomOAuth2User");
        CustomOAuth2User customUser = (CustomOAuth2User) result;
        assertEquals(existingUserId, customUser.getUserId(), "Should link to existing user ID");
        assertEquals(email, customUser.getEmail(), "Email should match");

        verify(sosialAccountsRepository).findByProviderAndProviderUserId(provider, providerId);
        verify(usersRepository).findByEmail(email);
        verify(usersRepository, never()).save(any(UsersEntity.class));
        verify(sosialAccountsRepository).save(any(SosialAccounts.class));
    }

    @Test
    void shouldCreateNewUserForNewEmail() {
        // Given
        String provider = "google";
        String providerId = "123456789";
        String email = "new@example.com";
        String name = "New User";
        UUID newUserId = UUID.randomUUID();

        UsersEntity newUser = UsersEntity.builder()
                .id(newUserId)
                .email(email)
                .username(name)
                .active(true)
                .build();

        when(userRequest.getClientRegistration()).thenReturn(mock(org.springframework.security.oauth2.client.registration.ClientRegistration.class));
        when(userRequest.getClientRegistration().getRegistrationId()).thenReturn(provider);
        when(oauth2User.getAttribute("id")).thenReturn(providerId);
        when(oauth2User.getAttribute("email")).thenReturn(email);
        when(oauth2User.getAttribute("name")).thenReturn(name);
        when(oauth2User.getAttribute("picture")).thenReturn("http://example.com/photo.jpg");
        when(sosialAccountsRepository.findByProviderAndProviderUserId(provider, providerId))
                .thenReturn(Optional.empty());
        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(newUser);

        // Mock the default service behavior
        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
        doReturn(oauth2User).when(spyService).loadUser(userRequest);

        // When
        OAuth2User result = spyService.loadUser(userRequest);

        // Then
        assertNotNull(result, "Result should not be null");
        assertTrue(result instanceof CustomOAuth2User, "Result should be CustomOAuth2User");
        CustomOAuth2User customUser = (CustomOAuth2User) result;
        assertEquals(newUserId, customUser.getUserId(), "Should create new user ID");
        assertEquals(email, customUser.getEmail(), "Email should match");

        verify(sosialAccountsRepository).findByProviderAndProviderUserId(provider, providerId);
        verify(usersRepository).findByEmail(email);
        verify(usersRepository).save(any(UsersEntity.class));
        verify(sosialAccountsRepository).save(any(SosialAccounts.class));
    }

    @Test
    void shouldHandleMissingAttributes() {
        // Given
        String provider = "google";
        String providerId = "123456789";

        when(userRequest.getClientRegistration()).thenReturn(mock(org.springframework.security.oauth2.client.registration.ClientRegistration.class));
        when(userRequest.getClientRegistration().getRegistrationId()).thenReturn(provider);
        when(oauth2User.getAttribute("id")).thenReturn(providerId);
        when(oauth2User.getAttribute("email")).thenReturn(null);
        when(oauth2User.getAttribute("name")).thenReturn(null);
        when(sosialAccountsRepository.findByProviderAndProviderUserId(provider, providerId))
                .thenReturn(Optional.empty());
        when(usersRepository.findByEmail(null)).thenReturn(Optional.empty());

        // Mock the default service behavior
        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
        doReturn(oauth2User).when(spyService).loadUser(userRequest);

        // When & Then
        assertThrows(NullPointerException.class, () -> spyService.loadUser(userRequest),
                "Should handle null attributes gracefully or throw appropriate exception");
    }

    @Test
    void shouldNormalizeEmailToLowercase() {
        // Given
        String provider = "google";
        String providerId = "123456789";
        String email = "Test@Example.COM";
        String normalizedEmail = "test@example.com";
        String name = "Test User";
        UUID userId = UUID.randomUUID();

        UsersEntity user = UsersEntity.builder()
                .id(userId)
                .email(normalizedEmail)
                .build();

        when(userRequest.getClientRegistration()).thenReturn(mock(org.springframework.security.oauth2.client.registration.ClientRegistration.class));
        when(userRequest.getClientRegistration().getRegistrationId()).thenReturn(provider);
        when(oauth2User.getAttribute("id")).thenReturn(providerId);
        when(oauth2User.getAttribute("email")).thenReturn(email);
        when(oauth2User.getAttribute("name")).thenReturn(name);
        when(sosialAccountsRepository.findByProviderAndProviderUserId(provider, providerId))
                .thenReturn(Optional.empty());
        when(usersRepository.findByEmail(normalizedEmail)).thenReturn(Optional.of(user));

        // Mock the default service behavior
        CustomOAuth2UserService spyService = spy(customOAuth2UserService);
        doReturn(oauth2User).when(spyService).loadUser(userRequest);

        // When
        OAuth2User result = spyService.loadUser(userRequest);

        // Then
        CustomOAuth2User customUser = (CustomOAuth2User) result;
        assertEquals(normalizedEmail, customUser.getEmail(), "Email should be normalized to lowercase");

        verify(usersRepository).findByEmail(normalizedEmail);
    }
}