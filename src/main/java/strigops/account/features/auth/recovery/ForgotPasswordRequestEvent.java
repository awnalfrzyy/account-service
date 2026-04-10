package strigops.account.features.auth.recovery;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
public record ForgotPasswordRequestEvent(

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 255, message = "Email is too long")
        String email,

        @NotBlank(message = "token required")
        String token,

        LocalDateTime requestedAt
) {
    public ForgotPasswordRequestEvent(
            String email,
            String token
    ){
        this(email,token,LocalDateTime.now());
    }
}
