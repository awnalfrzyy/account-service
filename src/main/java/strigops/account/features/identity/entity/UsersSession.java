package strigops.account.features.identity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersSession {
    @Id
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    private String refreshToken;
    private String userAgent;
    private String ipAddress;

    @Builder.Default
    @Column(name = "mfa_verified")
    private boolean mfaVerified = false;

    @Builder.Default
    @Column(name = "revoked")
    private boolean revoked = false;

    private String mfaChallengeToken;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime lastActivityAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt) || revoked;
    }
}
