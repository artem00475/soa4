package itmo.tuchin.nikitin.first_service.validation.constraint;

import itmo.tuchin.nikitin.first_service.validation.validator.ColorEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ColorEnumValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ColorEnum {
    String message() default "invalid color value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
