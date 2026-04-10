package strigops.account.features.auth.recovery.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import strigops.account.common.ValidPassword;

public record ResetPasswordCommand(
        @NotBlank(message = "token required")
        String token,

        @NotBlank(message = "The new password cannot be empty")
        @ValidPassword
        String newPassword
        ) {

}
