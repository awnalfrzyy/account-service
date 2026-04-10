package strigops.account.features.auth.recovery;

import java.time.LocalDateTime;

public record PasswordResetEvent(
        String email,
        String username,
        LocalDateTime resetAt
) {
    public PasswordResetEvent(
            String email,
            String username
    ){
        this(email, username, LocalDateTime.now());
    }
}
