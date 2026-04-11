package strigops.account.features.auth.register.users.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserCommand(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String username,

        String ipAddress,
        String cellModel,
        String countryCode,

        String placeName,
        Double latitude,
        Double longitude,
        String formattedAddress
) {}