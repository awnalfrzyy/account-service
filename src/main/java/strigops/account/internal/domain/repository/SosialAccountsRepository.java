package strigops.account.internal.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import strigops.account.internal.domain.entity.SosialAccounts;

@Repository
public interface SosialAccountsRepository extends JpaRepository<SosialAccounts, UUID> {

    Optional<SosialAccounts> findByProviderAndProviderUserId(String provider, String providerUserId);

    boolean existsByProviderAndProviderUserId(String provider, String providerUserId);
}
