//package strigops.account.common.validator;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import strigops.account.features.identity.repository.OrganizationRepository;
//
//@Component
//@RequiredArgsConstructor
//public class OrganizationEmailValidator {
//
//    private final OrganizationRepository organizationRepository;
//
//    public void validateDomain(String email) {
//        String domain = email.substring(email.indexOf("@") + 1);
//
//        boolean isRegistered = organizationRepository.existsByDomainNameAndIsVerifiedTrue(domain);
//
//        if (!isRegistered) {
//            throw new RuntimeException("Domain @" + domain + " is not authorized. Company must complete document verification first.");
//        }
//    }
//}
