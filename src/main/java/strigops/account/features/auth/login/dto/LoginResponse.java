package strigops.account.features.auth.login.dto;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(
        UUID userId,
        String email,
        boolean mfaRequired,
        String mfaToken,
        String accessToken,
        String refreshToken,
        String message
        ) {
        public static LoginResponse mfaRequired(String mfaToken){
                return new LoginResponse(
                        null,
                        null,
                        true,
                        mfaToken,
                        null,
                        null, "MFA Required" );
        }

        public static LoginResponse success(UUID userId, String email, String access, String refresh) {
                return new LoginResponse(
                        userId,
                        email,
                        false,
                        null,
                        access,
                        refresh,
                        "Login Success");
        }
}
