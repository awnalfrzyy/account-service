// package strigops.account.common;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import strigops.account.internal.domain.repository.UsersRepository;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class UniqueEmailValidatorTest {

//     @Mock
//     private UsersRepository usersRepository;

//     @InjectMocks
//     private UniqueEmailValidator validator;

//     @Test
//     void shouldValidateUniqueEmail() {
//         // Given
//         String email = "test@example.com";
//         when(usersRepository.existsByEmail(email.toLowerCase())).thenReturn(false);

//         // When
//         boolean isValid = validator.isValid(email, null);

//         // Then
//         assertTrue(isValid, "Unique email should be valid");
//         verify(usersRepository).existsByEmail(email.toLowerCase());
//     }

//     @Test
//     void shouldRejectDuplicateEmail() {
//         // Given
//         String email = "test@example.com";
//         when(usersRepository.existsByEmail(email.toLowerCase())).thenReturn(true);

//         // When
//         boolean isValid = validator.isValid(email, null);

//         // Then
//         assertFalse(isValid, "Duplicate email should be invalid");
//         verify(usersRepository).existsByEmail(email.toLowerCase());
//     }

//     @Test
//     void shouldRejectNullEmail() {
//         // Given
//         String email = null;

//         // When
//         boolean isValid = validator.isValid(email, null);

//         // Then
//         assertFalse(isValid, "Null email should be invalid");
//     }

//     @Test
//     void shouldNormalizeEmailToLowercase() {
//         // Given
//         String email = "Test@Example.COM";
//         when(usersRepository.existsByEmail("test@example.com")).thenReturn(false);

//         // When
//         boolean isValid = validator.isValid(email, null);

//         // Then
//         assertTrue(isValid, "Email should be normalized to lowercase");
//         verify(usersRepository).existsByEmail("test@example.com");
//     }

//     @Test
//     void shouldHandleEmptyEmail() {
//         // Given
//         String email = "";

//         // When
//         boolean isValid = validator.isValid(email, null);

//         // Then
//         assertFalse(isValid, "Empty email should be invalid");
//     }

//     @Test
//     void shouldHandleEmailWithSpaces() {
//         // Given
//         String email = " test@example.com ";
//         when(usersRepository.existsByEmail("test@example.com")).thenReturn(false);

//         // When
//         boolean isValid = validator.isValid(email, null);

//         // Then
//         assertTrue(isValid, "Email should be trimmed and validated");
//         verify(usersRepository).existsByEmail("test@example.com");
//     }
// }
