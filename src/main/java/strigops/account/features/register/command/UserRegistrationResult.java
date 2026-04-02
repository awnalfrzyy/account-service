package strigops.account.features.register.command;

import java.util.UUID;

public record UserRegistrationResult(
        UUID userId,
        String email
        ) {

}
