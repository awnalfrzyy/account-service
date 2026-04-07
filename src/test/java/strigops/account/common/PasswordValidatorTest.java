// package strigops.account.common;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;

// import static org.junit.jupiter.api.Assertions.*;

// @ExtendWith(MockitoExtension.class)
// class PasswordValidatorTest {

//     private final PasswordValidator validator = new PasswordValidator();

//     @Test
//     void shouldValidateStrongPassword() {
//         // Given
//         String strongPassword = "StrongPass123!";

//         // When
//         boolean isValid = validator.isValid(strongPassword, null);

//         // Then
//         assertTrue(isValid, "Strong password should be valid");
//     }

//     @Test
//     void shouldRejectPasswordWithoutLowercase() {
//         // Given
//         String password = "STRONGPASS123!";

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertFalse(isValid, "Password without lowercase should be invalid");
//     }

//     @Test
//     void shouldRejectPasswordWithoutUppercase() {
//         // Given
//         String password = "strongpass123!";

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertFalse(isValid, "Password without uppercase should be invalid");
//     }

//     @Test
//     void shouldRejectPasswordWithoutDigit() {
//         // Given
//         String password = "StrongPass!";

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertFalse(isValid, "Password without digit should be invalid");
//     }

//     @Test
//     void shouldRejectPasswordWithoutSpecialCharacter() {
//         // Given
//         String password = "StrongPass123";

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertFalse(isValid, "Password without special character should be invalid");
//     }

//     @Test
//     void shouldRejectPasswordTooShort() {
//         // Given
//         String password = "Str1!";

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertFalse(isValid, "Password shorter than 12 characters should be invalid");
//     }

//     @Test
//     void shouldRejectNullPassword() {
//         // Given
//         String password = null;

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertFalse(isValid, "Null password should be invalid");
//     }

//     @Test
//     void shouldRejectEmptyPassword() {
//         // Given
//         String password = "";

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertFalse(isValid, "Empty password should be invalid");
//     }

//     @Test
//     void shouldAcceptPasswordWithMinimumLength() {
//         // Given
//         String password = "StrongPass123!"; // Exactly 12 characters

//         // When
//         boolean isValid = validator.isValid(password, null);

//         // Then
//         assertTrue(isValid, "Password with minimum length should be valid");
//     }

//     @Test
//     void shouldAcceptPasswordWithVariousSpecialCharacters() {
//         // Given
//         String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "="};

//         for (String specialChar : specialChars) {
//             String password = "StrongPass123" + specialChar;

//             // When
//             boolean isValid = validator.isValid(password, null);

//             // Then
//             assertTrue(isValid, "Password with special character '" + specialChar + "' should be valid");
//         }
//     }
// }
