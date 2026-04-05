package strigops.account.features.auth.login.dto;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String email,
        String token
        ) {

}
