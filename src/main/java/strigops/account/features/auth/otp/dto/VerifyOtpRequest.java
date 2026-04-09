package strigops.account.features.auth.otp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyOtpRequest(
        @NotBlank(message = "Email jangan kosong dong")
        @Email(message = "Format email salah")
        String email,

        @NotBlank(message = "OTP harus diisi")
        @Size(min = 6, max = 6, message = "OTP harus 6 digit")
        String otp,

        String purpose
) {}