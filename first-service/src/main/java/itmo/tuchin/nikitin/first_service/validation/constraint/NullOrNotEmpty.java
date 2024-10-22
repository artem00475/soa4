package itmo.tuchin.nikitin.first_service.validation.constraint;

import itmo.tuchin.nikitin.first_service.validation.validator.NullOrNotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NullOrNotEmptyValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotEmpty {
    String message() default "invalid name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
