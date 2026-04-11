package strigops.account.internal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import strigops.account.features.auth.register.users.UserRegistrationService;
import strigops.account.features.auth.register.users.command.CreateUserCommand;
import strigops.account.features.auth.register.users.command.UserRegistrationResult;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class RegisterController {

    private final UserRegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResult> register(@Valid @RequestBody CreateUserCommand command){
        var result = registrationService.register(command);
        return ResponseEntity.ok(result);
    }
}
