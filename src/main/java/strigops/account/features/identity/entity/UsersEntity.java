package strigops.account.features.identity.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import strigops.account.features.identity.entity.extension.UserStatus;
import strigops.account.features.identity.entity.extension.UsersGender;
import strigops.account.features.identity.entity.extension.UsersPlatform;
import strigops.account.features.identity.entity.extension.UsersRole;

@Getter
@Setter
@Entity
@Table(name = "users_entity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 255, message = "Email maximum 255 characters")
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Column(nullable = false)
    private String password;

    @Size(max = 50, message = "Username maximum 50 characters")
    @Column(nullable = false)
    private String username;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UsersGender gender = UsersGender.OTHER;

    @Column(name = "last_ip")
    private String lastIp;

    @Column(name = "last_cell_model")
    private String lastCell_model;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "formatter_address", columnDefinition = "TEXT")
    private String formattedAddress;

    @Column(name = "photo_profile", columnDefinition = "TEXT")
    private String photoProfile;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "users_role")
    @Builder.Default
    private UsersRole role = UsersRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "enter_via")
    @Builder.Default
    private UsersPlatform enterVia = UsersPlatform.MANUAL;

    @Builder.Default
    @Column(name = "mfa_enabled")
    private boolean mfaEnable = false;

    @Column(nullable = true)
    private String mfaSecret;

    @CreationTimestamp
    @Column(name = "last_active", updatable = false)
    private LocalDateTime lastActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
