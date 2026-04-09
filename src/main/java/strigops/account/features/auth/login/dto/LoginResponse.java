package strigops.account.features.auth.login.dto;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(
        UUID userId,
        String email,
        String accessToken,
        String refreshToken,
        String message
        ) {
        public static LoginResponse success(UUID userId, String email, String access, String refresh) {
                return new LoginResponse(
                        userId,
                        email,
                        access,
                        refresh,
                        "Login Successful"
                );
        }

        public static LoginResponse requireOtp(String email){
                return new LoginResponse(
                        null,
                        email,
                        null,
                        null,
                        "Please check your email for OTP verification"
                );
        }
}
