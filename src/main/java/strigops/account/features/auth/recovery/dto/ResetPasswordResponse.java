package strigops.account.features.auth.recovery.dto;

public record ResetPasswordResponse(
        String accessToken,
        String message
        ) {

}
