package strigops.account.features.auth.recovery.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "token required")
        String token,

        @NotBlank(message = "The new password cannot be empty")
        String newPassword
        ) {

}
