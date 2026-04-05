package strigops.account.features.auth.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendOtpRequest(
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be empty")
        String email
        ) {

}
