package itmo.tuchin.nikitin.first_service.specification;

import itmo.tuchin.nikitin.first_service.entity.Person;
import itmo.tuchin.nikitin.first_service.exceptions.InvalidFilterParameterException;
import itmo.tuchin.nikitin.first_service.specification.specifications.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PersonFilter {
    private static final String availableOperations = Arrays.stream(Operations.values()).map(Enum::toString).collect(Collectors.joining("|"));
    private static final String notAvailableSymbols = Arrays.stream(Operations.values()).map(Enum::toString).collect(Collectors.joining(""));
    private static final Pattern filterValuePattern = Pattern.compile("^(" + availableOperations + ")([^" + notAvailableSymbols + "]+)$");

    public static Specification<Person> getPersonSpecificationForField(@NotEmpty String k, @NotEmpty String v) {
        Matcher matcher = filterValuePattern.matcher(v);
        if (!matcher.find()) {
            throw new InvalidFilterParameterException(k);
        }
        Operations operation = Operations.findByString(matcher.group(1));
        Fields field = Fields.findByString(k);
        String value = matcher.group(2);
        return switch (field) {
            case NAME, LOCATION_NAME -> new PersonSpecificationString(field, operation, value);
            case CREATION_DATE -> new PersonSpecificationDate(field, operation, value);
            case EYE_COLOR, HAIR_COLOR, NATIONALITY -> new PersonSpecificationEnum(field, operation, value);
            case ID, HEIGHT -> new PersonSpecificationInteger(field, operation, value);
            case COORDINATES_X, LOCATION_Y -> new PersonSpecificationDouble(field, operation, value);
            case COORDINATES_Y -> new PersonSpecificationLong(field, operation, value);
            case LOCATION_X -> new PersonSpecificationFloat(field, operation, value);
        };
    }

    public static Specification<Person> filterBy(List<Specification<Person>> specificationList) {
        return Specification.allOf(specificationList);
    }
}

