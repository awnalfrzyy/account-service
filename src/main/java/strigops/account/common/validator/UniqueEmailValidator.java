package strigops.account.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import strigops.account.common.validator.annotation.UniqueEmail;
import strigops.account.features.identity.repository.UsersRepository;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }

        return !usersRepository.existsByEmail(email.trim().toLowerCase());
    }
}