package strigops.account.features.auth.register.users.dto;

import java.util.UUID;

public record RegisterUserResponse(UUID id, String email, String username) {

}
