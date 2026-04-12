package strigops.account.common.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import strigops.account.common.validator.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email already registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
