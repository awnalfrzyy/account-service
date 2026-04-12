//package strigops.account.features.auth.register.organization;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import strigops.account.common.validator.FileValidator;
//import strigops.account.features.auth.register.organization.command.CreateOrganizationCommand;
//import strigops.account.features.auth.register.organization.command.OrganizationRegistrationResult;
//import strigops.account.features.identity.entity.OrganizationEntity;
//import strigops.account.features.identity.repository.OrganizationRepository;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class OrganizationRegistrationService {
//
//    private final OrganizationRepository organizationRepository;
//
//    @Transactional
//    public OrganizationRegistrationResult registerOrganization(CreateOrganizationCommand command) {
//        if (command.documentBytes() != null) {
//            if (!FileValidator.isFileSizeValid(command.documentBytes())) throw new RuntimeException("File too large!");
//            if (!FileValidator.isFormatValid(command.contentType())) throw new RuntimeException("Invalid format!");
//            if (!FileValidator.isImageSignatureValid(command.documentBytes())) throw new RuntimeException("Fake image!");
//        }
//
//        if (organizationRepository.findByDomainName(command.domainName().toLowerCase()).isPresent()) {
//            throw new RuntimeException("Domain is already registered.");
//        }
//
//        OrganizationEntity organization = OrganizationEntity.builder()
//                .organizationName(command.organizationName())
//                .domainName(command.domainName().toLowerCase())
//                .isVerified(false)
//                .build();
//
//        OrganizationEntity saved = organizationRepository.save(organization);
//
//        return new OrganizationRegistrationResult(saved.getId(), "PENDING", "Success");
//    }
//
//
//    @Transactional(readOnly = true)
//    public boolean validateAccess(String identifier) {
//        if (identifier.contains(".")) {
//            return organizationRepository.existsByDomainNameAndIsVerifiedTrue(identifier.toLowerCase());
//        }
//        return organizationRepository.existsByLicenseKeyActive(identifier);
//    }
//
//    @Transactional(readOnly = true)
//    public OrganizationEntity getOrganization(UUID id) {
//        return organizationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Organization not found"));
//    }
//
//    @Transactional
//    public void verifyOrganization(UUID id, boolean status) {
//        OrganizationEntity org = getOrganization(id);
//        org.setVerified(status);
//        organizationRepository.save(org);
//    }
//
//    @Transactional
//    public void deleteOrganization(UUID id) {
//        organizationRepository.deleteById(id);
//    }
//}