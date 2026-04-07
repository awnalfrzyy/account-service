// package strigops.account.internal.infrastructure.config;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;

// import strigops.account.internal.domain.entity.UsersEntity;
// import strigops.account.internal.domain.repository.UsersRepository;

// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class UserDetailsServiceImplTest {

//     @Mock
//     private UsersRepository usersRepository;

//     @InjectMocks
//     private UserDetailsServiceImpl userDetailsService;

//     @Test
//     void shouldLoadUserByUsernameSuccessfully() {
//         String email = "test@example.com";
//         String password = "encodedPassword";
//         UsersEntity user = UsersEntity.builder()
//                 .id(UUID.randomUUID())
//                 .email(email)
//                 .password(password)
//                 .active(true)
//                 .build();

//         when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

//         var userDetails = userDetailsService.loadUserByUsername(email);

//         assertNotNull(userDetails, "UserDetails should not be null");
//         assertEquals(email, userDetails.getUsername(), "Username should match email");
//         assertEquals(password, userDetails.getPassword(), "Password should match");
//         assertTrue(userDetails.getAuthorities().isEmpty(), "Authorities should be empty for basic user");

//         verify(usersRepository).findByEmail(email);
//     }

//     @Test
//     void shouldThrowExceptionWhenUserNotFound() {
//         String email = "nonexistent@example.com";
//         when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

//         UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
//                 () -> userDetailsService.loadUserByUsername(email),
//                 "Should throw UsernameNotFoundException for non-existent user");

//         assertTrue(exception.getMessage().contains(email), "Exception message should contain the email");

//         verify(usersRepository).findByEmail(email);
//     }

//     @Test
//     void shouldNormalizeEmailToLowercase() {
//         String inputEmail = "Test@Example.COM";
//         String normalizedEmail = "test@example.com";
//         String password = "encodedPassword";
//         UsersEntity user = UsersEntity.builder()
//                 .id(UUID.randomUUID())
//                 .email(normalizedEmail)
//                 .password(password)
//                 .active(true)
//                 .build();

//         when(usersRepository.findByEmail(normalizedEmail)).thenReturn(Optional.of(user));
//         var userDetails = userDetailsService.loadUserByUsername(inputEmail);
//         assertEquals(normalizedEmail, userDetails.getUsername(), "Username should be normalized to lowercase");

//         verify(usersRepository).findByEmail(normalizedEmail);
//     }

//     @Test
//     void shouldHandleNullEmail() {
//         String email = null;
//         UsernameNotFoundException ex = assertThrows(
//                 UsernameNotFoundException.class,
//                 () -> userDetailsService.loadUserByUsername(email)
//         );

//         assertTrue(ex.getMessage().contains("Email"));
//     }

//     @Test
//     void shouldHandleEmptyEmail() {
//         String email = "";
//         UsernameNotFoundException ex = assertThrows(
//                 UsernameNotFoundException.class,
//                 () -> userDetailsService.loadUserByUsername(email)
//         );
//         assertTrue(ex.getMessage().contains("Email"));
//     }

//     @Test
//     void shouldHandleBlankEmail() {
//         String email = "   ";
//         UsernameNotFoundException ex = assertThrows(
//                 UsernameNotFoundException.class,
//                 () -> userDetailsService.loadUserByUsername(email)
//         );

//         assertTrue(ex.getMessage().contains("Email"));
//     }

//     @Test
//     void shouldHandleUserWithSpecialCharactersInEmail() {
//         // Given
//         String email = "test+tag@example.com";
//         String password = "encodedPassword";
//         UsersEntity user = UsersEntity.builder()
//                 .id(UUID.randomUUID())
//                 .email(email)
//                 .password(password)
//                 .active(true)
//                 .build();

//         when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

//         // When
//         var userDetails = userDetailsService.loadUserByUsername(email);

//         // Then
//         assertEquals(email, userDetails.getUsername(), "Should handle emails with special characters");

//         verify(usersRepository).findByEmail(email);
//     }

//     @Test
//     void shouldTrimEmailBeforeProcessing() {
//         String inputEmail = "  test@example.com  ";
//         String normalizedEmail = "test@example.com";
//         String password = "encodedPassword";

//         UsersEntity user = UsersEntity.builder()
//                 .id(UUID.randomUUID())
//                 .email(normalizedEmail)
//                 .password(password)
//                 .active(true)
//                 .build();

//         when(usersRepository.findByEmail(normalizedEmail)).thenReturn(Optional.of(user));

//         var userDetails = userDetailsService.loadUserByUsername(inputEmail);

//         assertEquals(normalizedEmail, userDetails.getUsername());

//         verify(usersRepository).findByEmail(normalizedEmail);
//     }

//     @Test
//     void shouldHandleInactiveUser() {
//         // Given
//         String email = "inactive@example.com";
//         String password = "encodedPassword";
//         UsersEntity user = UsersEntity.builder()
//                 .id(UUID.randomUUID())
//                 .email(email)
//                 .password(password)
//                 .active(false) // User is inactive
//                 .build();

//         when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

//         // When
//         var userDetails = userDetailsService.loadUserByUsername(email);

//         // Then
//         assertNotNull(userDetails, "Should still load inactive user (security framework handles activation)");
//         assertEquals(email, userDetails.getUsername(), "Username should match");

//         verify(usersRepository).findByEmail(email);
//     }
// }
