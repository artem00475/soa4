package itmo.tuchin.nikitin.first_service.validation.validator;

import itmo.tuchin.nikitin.first_service.validation.constraint.NullOrNotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, String> {
    @Override
    public void initialize(NullOrNotEmpty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) {
            return true;
        } else {
            return !s.trim().isEmpty();
        }
    }
}
