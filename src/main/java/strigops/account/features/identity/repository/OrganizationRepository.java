//package strigops.account.features.identity.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import strigops.account.features.identity.entity.OrganizationEntity;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Repository
//public interface OrganizationRepository extends JpaRepository<OrganizationEntity, UUID> {
//
//    boolean existsByDomainNameAndIsVerifiedTrue(String domainName);
//
//    Optional<OrganizationEntity> findByDomainName(String domainName);
//
//    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END" +
//            "FROM OrganizationEntity c WHERE c.domainName = :domain AND c.isVerified = true")
//    boolean isOrganizationAuthorized(@Param("domain") String domain);
//
//    @Query(value = "SELECT EXISTS(SELECT 1 FROM organization_licenses WHERE license_key = :key AND is_active = true)", nativeQuery = true)
//    boolean existsByLicenseKeyActive(@Param("key") String key);
//}
