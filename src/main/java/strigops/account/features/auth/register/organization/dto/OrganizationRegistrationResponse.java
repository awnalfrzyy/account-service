package strigops.account.features.auth.register.organization.dto;

import java.util.UUID;

public record OrganizationRegistrationResponse(
        UUID id,
        String status,
        String message
) {
}
