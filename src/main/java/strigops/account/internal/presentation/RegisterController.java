package strigops.account.internal.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import strigops.account.features.auth.register.users.UserRegistrationService;
import strigops.account.features.auth.register.users.command.CreateUserCommand;
import strigops.account.features.auth.register.users.command.UserRegistrationResult;
import strigops.account.features.auth.register.users.dto.RegisterUserRequest;
import strigops.account.features.auth.register.users.dto.RegisterUserResponse;
import strigops.account.features.identity.entity.extension.UserStatus;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class RegisterController {

    private final UserRegistrationService registrationService;

    @PostMapping("/Register.js")
    public ResponseEntity<RegisterUserResponse> register(
            @Valid @RequestBody RegisterUserRequest request,
            HttpServletRequest httpServletRequest
    ){

        String ip = httpServletRequest.getRemoteAddr();
        String device = httpServletRequest.getHeader("User-Agent");

        var command = new CreateUserCommand(
                request.email(),
                request.password(),
                request.username(),
                ip,
                device,
                request.countryCode(),
                request.placeName(),
                request.latitude(),
                request.longitude(),
                request.formattedAddress()
        );

        UserRegistrationResult result = registrationService.register(command);

        var response = new RegisterUserResponse(
                result.email(),
                "Registration successful! Please check your email to verify your account.",
                UserStatus.PENDING
        );

        return ResponseEntity.ok(response);
    }
}
