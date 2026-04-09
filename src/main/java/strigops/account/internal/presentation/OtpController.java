package strigops.account.internal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import strigops.account.features.auth.login.LoginService;
import strigops.account.features.auth.login.dto.LoginResponse;
import strigops.account.features.auth.otp.dto.VerifyOtpRequest;
import strigops.account.features.auth.otp.OtpService;
import strigops.account.features.auth.register.UserRegistrationService;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.repository.UsersRepository;
import strigops.account.features.session.SessionService;

import java.time.Duration;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final UserRegistrationService registrationService;
    private final LoginService loginService;
    private final UsersRepository usersRepository;
    private final SessionService sessionService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request){
        boolean isValid = otpService.verifyOtp(
                request.email(),
                request.otp(),
                request.purpose()
                );

        if(!isValid){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("OTP is wrong or your code has expired");
        }

        if("LOGIN".equals(request.purpose())){
            UsersEntity usersEntity = usersRepository.findByEmail(request.email()).orElseThrow();
            LoginResponse loginResponse = sessionService.handleLogin(usersEntity);
            ResponseCookie responseCookie = ResponseCookie.from("refresh_token", loginResponse.refreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(loginResponse);
        }

        if ("REGISTER".equals(request.purpose())) {
            loginService.enableUser(request.email());
            return ResponseEntity.ok("Account verified and activated. Please login.");
        }

        registrationService.enableUser(request.email());
        loginService.enableUser(request.email());
        return ResponseEntity.ok("Verification successful, account is now active!");
    }
}
