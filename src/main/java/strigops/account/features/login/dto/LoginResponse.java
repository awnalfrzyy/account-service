package strigops.account.features.login.dto;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String email,
        String token
        ) {

}
