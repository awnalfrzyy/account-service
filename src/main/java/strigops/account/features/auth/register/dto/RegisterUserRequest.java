package strigops.account.features.auth.register.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import strigops.account.common.UniqueEmail;
import strigops.account.common.ValidPassword;

public record RegisterUserRequest(
        @NotBlank(message = "Email tidak boleh kosong")
        @Email(message = "Email tidak valid")
        @Size(max = 255, message = "Email maksimal 255 karakter")
        @UniqueEmail
        String email,
        @NotBlank(message = "Password tidak boleh kosong")
        @ValidPassword
        String password,
        @Size(max = 50, message = "Username maksimal 50 karakter")
        String username
        ) {

}
