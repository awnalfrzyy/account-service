package strigops.account.internal.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import strigops.account.features.auth.changePassword.ChangePasswordService;
import strigops.account.features.auth.changePassword.dto.ChangePasswordRequest;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class ChangePasswordController {

    private final ChangePasswordService changePasswordService;

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordService.changePassword(request);

        return ResponseEntity.ok("Password successfully changed. Please check your email for confirmation.");
    }
}