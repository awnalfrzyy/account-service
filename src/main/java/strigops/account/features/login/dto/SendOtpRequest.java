package strigops.account.features.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendOtpRequest(
        @Email(message = "Email harus valid")
        @NotBlank(message = "Email tidak boleh kosong")
        String email
        ) {

}
