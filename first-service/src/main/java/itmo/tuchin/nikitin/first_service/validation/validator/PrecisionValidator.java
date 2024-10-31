package itmo.tuchin.nikitin.first_service.validation.validator;

import itmo.tuchin.nikitin.first_service.validation.constraint.Precision;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PrecisionValidator implements ConstraintValidator<Precision, String> {
    int precision;

    @Override
    public void initialize(Precision constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        precision = constraintAnnotation.precision();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) {
            return true;
        } else {
            BigDecimal bigDecimal = new BigDecimal(s);
            return bigDecimal.precision() <= precision;
        }
    }
}
