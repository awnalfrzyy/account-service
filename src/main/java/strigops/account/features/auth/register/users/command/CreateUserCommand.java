package strigops.account.features.auth.register.users.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import strigops.account.common.validator.EmailValidator;
import strigops.account.common.validator.PasswordValidator;
import strigops.account.common.validator.UsernameValidator;
import strigops.account.common.validator.UniqueEmailValidator;
import strigops.account.common.validator.annotation.UniqueEmail;

public record CreateUserCommand(
        @Pattern(regexp = EmailValidator.REGEX, message = EmailValidator.MESSAGE)
        @UniqueEmail
        String email,

        @Pattern(regexp = PasswordValidator.REGEX, message = PasswordValidator.MESSAGE)
        @Size(min = 8)
        String password,

        @Pattern(regexp = UsernameValidator.REGEX, message = UsernameValidator.MESSAGE)
        String username,

        String ipAddress,
        String cellModel,
        String countryCode,

        String placeName,
        Double latitude,
        Double longitude,
        String formattedAddress
) {}