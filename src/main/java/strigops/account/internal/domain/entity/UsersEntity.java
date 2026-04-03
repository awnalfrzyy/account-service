package strigops.account.internal.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import strigops.account.common.ValidPassword;

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

    @Email(message = "Email harus valid")
    @NotBlank(message = "Email tidak boleh kosong")
    @Size(max = 255, message = "Email maksimal 255 karakter")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @ValidPassword
    @Column(nullable = false)
    private String password;

    @Size(max = 50, message = "Username maksimal 50 karakter")
    private String username;

    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
