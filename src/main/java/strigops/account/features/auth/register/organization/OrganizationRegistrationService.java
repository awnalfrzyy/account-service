package strigops.account.features.auth.register.organization;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strigops.account.common.validator.FileValidator;
import strigops.account.features.auth.register.organization.command.CreateOrganizationCommand;
import strigops.account.features.auth.register.organization.command.OrganizationRegistrationResult;
import strigops.account.features.identity.entity.OrganizationEntity;
import strigops.account.features.identity.repository.OrganizationRepository;

@Service
@RequiredArgsConstructor
public class OrganizationRegistrationService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationRegistrationResult register(CreateOrganizationCommand command) {
        if (command.documentBytes() != null) {
            if (!FileValidator.isFileSizeValid(command.documentBytes())) {
                throw new RuntimeException("File too large! Max 5MB.");
            }

            if (!FileValidator.isFormatValid(command.contentType())) {
                throw new RuntimeException("Invalid format! Only PNG/JPG/JPEG allowed.");
            }

            if (!FileValidator.isImageSignatureValid(command.documentBytes())) {
                throw new RuntimeException("Fake image detected! Signature mismatch.");
            }
        }

        if (organizationRepository.findByDomainName(command.domainName().toLowerCase()).isPresent()) {
            throw new RuntimeException("Domain is already registered by another organization.");
        }

        OrganizationEntity organization = OrganizationEntity.builder()
                .organizationName(command.organizationName())
                .domainName(command.domainName().toLowerCase())
                .isVerified(false)
                .build();

        OrganizationEntity saved = organizationRepository.save(organization);

        return new OrganizationRegistrationResult(
                saved.getId(),
                "PENDING",
                "Organization registered successfully. Please wait for document verification."
        );
    }
}