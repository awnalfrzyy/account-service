package strigops.account.features.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(
        @Email(message = "Email must be valid" + "")
        @NotBlank(message = "Email cannot be empty")
        String email,
        @NotBlank(message = "OTP cannot be empty")
        String otp
        ) {

}
