package strigops.account.features.auth.recovery.command;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordCommand(
        @NotBlank(message = "token required")
        String token,

        @NotBlank(message = "The new password cannot be empty")
        String newPassword
        ) {

}
