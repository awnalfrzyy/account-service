package strigops.account.features.auth.changePassword.dto;

import jakarta.validation.constraints.NotBlank;
import strigops.account.common.ValidPassword;

public record ChangePasswordRequest(
        String email,
        @NotBlank(message = "Old password cannot be empty")
        @ValidPassword
        String oldPassword,
        @NotBlank(message = "New password cannot be empty")
        @ValidPassword
        String newPassword
        ) {

}
