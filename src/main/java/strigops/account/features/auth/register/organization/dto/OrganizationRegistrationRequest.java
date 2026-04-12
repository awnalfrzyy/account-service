//package strigops.account.features.auth.register.organization.dto;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import strigops.account.common.validator.EmailValidator;
//
//public record OrganizationRegistrationRequest(
//
//        @NotBlank(message = "Organization name required")
//        String organizationName,
//
//        @NotBlank(message = "Domain is required")
//        @Pattern(regexp = "^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid domain format")
//        String domainName,
//
//        @NotBlank(message = "Admin email is required")
//        @Email(regexp = EmailValidator.REGEX)
//        String adminEmail,
//
//        String documentBase64,
//        String documentContentType
//) {
//}
