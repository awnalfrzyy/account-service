package strigops.account.features.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyOtpRequest(
        @Email(message = "Email harus valid")
        @NotBlank(message = "Email tidak boleh kosong")
        String email,
        @NotBlank(message = "OTP tidak boleh kosong")
        String otp
        ) {

}
