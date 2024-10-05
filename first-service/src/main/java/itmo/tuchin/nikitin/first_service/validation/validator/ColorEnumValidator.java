package itmo.tuchin.nikitin.first_service.validation.validator;

import itmo.tuchin.nikitin.first_service.entity.Color;
import itmo.tuchin.nikitin.first_service.validation.constraint.ColorEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ColorEnumValidator implements ConstraintValidator<ColorEnum, String> {
    @Override
    public void initialize(ColorEnum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) {
            return true;
        }
        s = s.toUpperCase().strip();
        try {
            Color.valueOf(s);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
