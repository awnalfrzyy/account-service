package strigops.account.internal.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import strigops.account.internal.domain.entity.UsersEntity;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {

    Optional<UsersEntity> findByEmail(String email);

    @Override
    @SuppressWarnings("unchecked")
    UsersEntity save(UsersEntity user);

    boolean existsByEmail(String email);
}
