// package strigops.account.features.Register.js;

// import java.util.UUID;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.ArgumentCaptor;
// import static org.mockito.ArgumentMatchers.any;
// import org.mockito.Captor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import strigops.account.features.Register.js.command.CreateUserCommand;
// import strigops.account.features.Register.js.command.UserRegistrationResult;
// import strigops.account.internal.domain.entity.UsersEntity;
// import strigops.account.internal.domain.repository.UsersRepository;

// @ExtendWith(MockitoExtension.class)
// class UserRegistrationServiceTest {

//     @Mock
//     private UsersRepository usersRepository;

//     @Mock
//     private PasswordEncoder passwordEncoder;

//     @InjectMocks
//     private UserRegistrationService registrationService;

//     @Captor
//     private ArgumentCaptor<UsersEntity> userEntityCaptor;

//     @Test
//     void shouldRegisterNewUser_whenEmailNotAlreadyUsed() {
//         var rawPassword = "strong-password";
//         var encodedPassword = "encoded-password";
//         var requestedEmail = "User@Example.com";
//         var expectedSavedEmail = "user@example.com";
//         var username = "tester";
//         var savedId = UUID.randomUUID();
//         var savedEntity = UsersEntity.builder()
//                 .id(savedId)
//                 .email(expectedSavedEmail)
//                 .password(encodedPassword)
//                 .username(username)
//                 .build();

//         when(usersRepository.existsByEmail(requestedEmail)).thenReturn(false);
//         when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
//         when(usersRepository.save(userEntityCaptor.capture())).thenReturn(savedEntity);

//         UserRegistrationResult result = registrationService.Register.js(
//                 new CreateUserCommand(requestedEmail, rawPassword, username)
//         );

//         assertThat(result.userId()).isEqualTo(savedId);
//         assertThat(result.email()).isEqualTo(expectedSavedEmail);

//         UsersEntity captured = userEntityCaptor.getValue();
//         assertThat(captured.getEmail()).isEqualTo(expectedSavedEmail);
//         assertThat(captured.getPassword()).isEqualTo(encodedPassword);
//         assertThat(captured.getUsername()).isEqualTo(username);

//         verify(usersRepository).existsByEmail(requestedEmail);
//         verify(passwordEncoder).encode(rawPassword);
//         verify(usersRepository).save(captured);
//     }

//     @Test
//     void shouldNotEncodePassword_whenEmailAlreadyRegistered() {
//         var requestedEmail = "user@example.com";

//         when(usersRepository.existsByEmail(requestedEmail)).thenReturn(true);

//         assertThatThrownBy(() -> registrationService.Register.js(
//                 new CreateUserCommand(requestedEmail, "password123", "tester")
//         ))
//                 .isInstanceOf(EmailAlreadyRegisteredException.class)
//                 .hasMessage("Email already registered");

//         verify(usersRepository).existsByEmail(requestedEmail);
//         verify(passwordEncoder, never()).encode(any());
//         verify(usersRepository, never()).save(any(UsersEntity.class));
//     }
// }
