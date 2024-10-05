package itmo.tuchin.nikitin.first_service.validation.constraint;

import itmo.tuchin.nikitin.first_service.validation.validator.CountryEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CountryEnumValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryEnum {
    String message() default "invalid country value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
