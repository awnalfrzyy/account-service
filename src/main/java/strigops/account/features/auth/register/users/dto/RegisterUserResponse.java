package strigops.account.features.auth.register.users.dto;

import strigops.account.features.identity.entity.extension.UserStatus;

import java.util.UUID;

public record RegisterUserResponse(
        String email,
        String messange,
        UserStatus status
        ) {

}
