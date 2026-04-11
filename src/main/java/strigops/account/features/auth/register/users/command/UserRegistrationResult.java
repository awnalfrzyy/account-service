package strigops.account.features.auth.register.users.command;

import java.util.UUID;

public record UserRegistrationResult(
        UUID userId,
        String email,
        String username
        ) {

}
