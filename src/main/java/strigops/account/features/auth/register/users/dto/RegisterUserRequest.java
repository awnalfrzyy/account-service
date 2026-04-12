package strigops.account.features.auth.register.users.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import strigops.account.common.validator.EmailValidator;
import strigops.account.common.validator.PasswordValidator;
import strigops.account.common.validator.UsernameValidator;
import strigops.account.common.validator.annotation.UniqueEmail;

public record RegisterUserRequest(
        @Size(max = 255, message = "Email maximum 255 characters")
        @Pattern(regexp = EmailValidator.REGEX, message = EmailValidator.MESSAGE)
        @UniqueEmail
        String email,

        @Pattern(regexp = PasswordValidator.REGEX, message = UsernameValidator.MESSAGE)
        String password,
        @Pattern(regexp = UsernameValidator.REGEX, message = UsernameValidator.MESSAGE)
        @Size(max = 50, message = "Username maximum 50 characters")
        String username,

        String countryCode,
        String placeName,
        Double latitude,
        Double longitude,
        String formattedAddress
        ) {

}
