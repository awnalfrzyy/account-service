package strigops.account.features.register.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "Email tidak boleh kosong")
        @Email(message = "Email tidak valid")
        @Size(max = 255, message = "Email maksimal 255 karakter")
        String email,
        @NotBlank(message = "Password tidak boleh kosong")
        @Size(min = 12, max = 100, message = "Password minimal 12 karakter")
        String password,
        @Size(max = 50, message = "Username maksimal 50 karakter")
        String username
        ) {

}
