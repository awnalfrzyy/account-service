package strigops.account.internal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import strigops.account.features.auth.login.LoginService;
import strigops.account.features.auth.login.command.LoginCommand;
import strigops.account.features.auth.login.dto.LoginRequest;
import strigops.account.features.auth.login.dto.LoginResponse;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
    LoginCommand command = new LoginCommand(request.email(), request.password());
    LoginResponse response = loginService.login(command);
    return ResponseEntity.ok(response);
    }
}
