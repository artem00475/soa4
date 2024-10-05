package itmo.tuchin.nikitin.first_service.validation.validator;

import itmo.tuchin.nikitin.first_service.entity.Color;
import itmo.tuchin.nikitin.first_service.entity.Country;
import itmo.tuchin.nikitin.first_service.validation.constraint.ColorEnum;
import itmo.tuchin.nikitin.first_service.validation.constraint.CountryEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CountryEnumValidator implements ConstraintValidator<CountryEnum, String> {
    @Override
    public void initialize(CountryEnum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) {
            return true;
        }
        s = s.toUpperCase().strip();
        try {
            Country.valueOf(s);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
