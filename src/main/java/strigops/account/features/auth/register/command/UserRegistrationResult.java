package strigops.account.features.auth.register.command;

import java.util.UUID;

public record UserRegistrationResult(
        UUID userId,
        String email
        ) {

}
