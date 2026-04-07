// package strigops.account;

// import static org.junit.jupiter.api.Assertions.assertAll;
// import static org.junit.jupiter.api.Assertions.assertNotNull;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;

// import strigops.account.internal.domain.repository.UsersRepository;
// import strigops.account.internal.infrastructure.config.JwtService;

// @SpringBootTest
// @ActiveProfiles("test")
// class AccountApplicationTests {

//     @Autowired
//     private UsersRepository usersRepository;

//     @Autowired
//     private JwtService jwtService;

//     @Test
//     void contextLoads() {
//         assertAll(
//                 () -> assertNotNull(usersRepository),
//                 () -> assertNotNull(jwtService)
//         );
//     }
// }
