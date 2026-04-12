//package strigops.account.internal.presentation;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import strigops.account.features.auth.register.organization.OrganizationRegistrationService;
//import strigops.account.features.auth.register.organization.command.CreateOrganizationCommand;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/organizations")
//@RequiredArgsConstructor
//public class OrganizationController {
//
//    private final OrganizationRegistrationService registrationService;
//
//    @PostMapping
//    public ResponseEntity<?> register(@RequestBody CreateOrganizationCommand command) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.registerOrganization(command));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getDetail(@PathVariable UUID id) {
//        return ResponseEntity.ok(registrationService.getOrganization(id));
//    }
//
//    @RequestMapping(value = "/access/{identifier}", method = RequestMethod.HEAD)
//    public ResponseEntity<Void> checkAccess(@PathVariable String identifier) {
//        boolean valid = registrationService.validateAccess(identifier);
//        return valid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//    }
//
//    @PatchMapping("/{id}/verify")
//    public ResponseEntity<Void> updateStatus(@PathVariable UUID id, @RequestParam boolean status) {
//        registrationService.verifyOrganization(id, status);
//        return ResponseEntity.noContent().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> remove(@PathVariable UUID id) {
//        registrationService.deleteOrganization(id);
//        return ResponseEntity.ok().build();
//    }
//}