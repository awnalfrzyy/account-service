package strigops.account.features.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import strigops.account.features.identity.entity.PasswordResetToken;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.entity.UsersSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(UsersEntity user);

    interface UserSessionsRepository extends JpaRepository <UsersSession, UUID>{
        Optional<UsersSession> findByRefreshToken(String token);
        List<UsersSession> findAllByUserAndRevokedFalse(UsersEntity user);
        void deleteByExpiresAtBefore(LocalDateTime now);
    }
}
