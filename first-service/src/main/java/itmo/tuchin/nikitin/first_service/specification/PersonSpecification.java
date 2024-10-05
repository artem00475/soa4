package itmo.tuchin.nikitin.first_service.specification;

import itmo.tuchin.nikitin.first_service.entity.Person;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonSpecification {
    public static final String NAME = "name";
    public static final String HEIGHT = "height";
    public static final String CREATION_DATE = "creationDate";
    public static final String EYE_COLOR = "eyeColor";
    public static final String HAIR_COLOR = "hairColor";
    public static final String NATIONALITY = "nationality";
    public static final String ID = "id";
    public static final String COORDINATES_X = "coordinates.x";
    public static final String COORDINATES_Y = "coordinates.y";
    public static final String LOCATION_X = "location.x";
    public static final String LOCATION_Y = "location.y";
    public static final String LOCATION_NAME = "location.name";


    private PersonSpecification() {

    }

    public static Specification<Person> filterBy(Map<String, String> filter) {
        return Specification
                .where(join())
                .and(filterField(NAME, filter.get(NAME)))
                .and(filterField(HEIGHT, filter.get(HEIGHT)))
                .and(filterField(CREATION_DATE, filter.get(CREATION_DATE)))
                .and(filterField(EYE_COLOR, filter.get(EYE_COLOR)))
                .and(filterField(NATIONALITY, filter.get(NATIONALITY)))
                .and(filterField(ID, filter.get(ID)))
                .and(filterField(HAIR_COLOR, filter.get(HAIR_COLOR)))
                .and(filterField(COORDINATES_X, filter.get(COORDINATES_X)))
                .and(filterField(COORDINATES_Y, filter.get(COORDINATES_Y)))
                .and(filterField(LOCATION_X, filter.get(LOCATION_X)))
                .and(filterField(LOCATION_Y, filter.get(LOCATION_Y)))
                .and(filterField(LOCATION_NAME, filter.get(LOCATION_NAME)));
    }

    private static Specification<Person> join() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (Long.class != criteriaQuery.getResultType()) {
                root.fetch("coordinates", JoinType.LEFT);
                root.fetch("location", JoinType.LEFT);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static Path resolvePath(Root<Person> root, String field) {
        String[] fieldSplit = field.split("\\.");
        if (fieldSplit.length == 2) {
           return root.get(fieldSplit[0]).get(fieldSplit[1]);
        } else {
           return root.get(fieldSplit[0]);
        }
    }

    private static Specification<Person> filterField(String field, String value) {
        if (value == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        Pattern pattern = Pattern.compile("^(|>|<|>=|<=|~|=)([\\w-]+)$");
        Matcher matcher = pattern.matcher(value);
        if (!matcher.find()) {
            return (root, query, cb) -> cb.conjunction();
        }
        String val = matcher.group(2);
        return switch (matcher.group(1)) {
            case ">" ->
                    ((root, query, cb) -> val == null || val.isEmpty() ? cb.conjunction() : cb.greaterThan(resolvePath(root, field), val));
            case ">=" ->
                    ((root, query, cb) -> val == null || val.isEmpty() ? cb.conjunction() : cb.greaterThanOrEqualTo(resolvePath(root, field), val));
            case "<" ->
                    ((root, query, cb) -> val == null || val.isEmpty() ? cb.conjunction() : cb.lessThan(resolvePath(root, field), val));
            case "<=" ->
                    ((root, query, cb) -> val == null || val.isEmpty() ? cb.conjunction() : cb.lessThanOrEqualTo(resolvePath(root, field), LocalDate.parse(val)));
            case "~" ->
                    ((root, query, cb) -> val == null || val.isEmpty() ? cb.conjunction() : cb.like(resolvePath(root, field), "%" + val + "%"));
            default ->
                    ((root, query, cb) -> val == null || val.isEmpty() ? cb.conjunction() : cb.equal(resolvePath(root, field), val));
        };
    }
}

