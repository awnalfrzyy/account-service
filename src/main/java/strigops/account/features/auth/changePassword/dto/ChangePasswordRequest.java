package strigops.account.features.auth.changePassword.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        String email,
        @NotBlank(message = "Old password cannot be empty")
        String oldPassword,
        @NotBlank(message = "New password cannot be empty")
        String newPassword
        ) {

}
