package strigops.account.features.identity.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strigops.account.features.identity.entity.UsersEntity;
import strigops.account.features.identity.entity.UsersSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersSessionRepostory extends JpaRepository<UsersSession, UUID> {
   Optional<UsersSession> findByRefreshToken(String refreshToken);

   List<UsersSession> findByUserAndRevokedFalse(UsersEntity user);

   Optional<UsersSession> findBySessionIdAndMfaVerifiedFalse(UUID sessionId);

   @Modifying
   @Query("DELETE FROM UsersSession s WHERE s.expiresAt < :now")
   void deleteAllExpiredSince(@Param("now") LocalDateTime now);

   @Modifying
   @Query("UPDATE UsersSession s SET s.revoked = true WHERE s.sessionId = :sessionId")
   void revokedSession(@Param("sessionId") UUID sessionId);
}
