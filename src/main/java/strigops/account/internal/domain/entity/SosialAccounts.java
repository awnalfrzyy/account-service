package strigops.account.internal.domain.entity;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sosial_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SosialAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerUserId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String profilePictureUrl;

    @Column(nullable = false)
    private String accessToken;

    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private java.time.LocalDateTime createdAt;

    @CreationTimestamp
    @Column(updatable = false)
    private java.time.LocalDateTime updatedAt;

    @Column(nullable = true)
    private java.time.LocalDateTime deletedAt;
}
