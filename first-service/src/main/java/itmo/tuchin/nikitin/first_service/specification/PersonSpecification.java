package itmo.tuchin.nikitin.first_service.specification;

import itmo.tuchin.nikitin.first_service.entity.Person;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@AllArgsConstructor
public class PersonSpecification implements Specification<Person> {

    private PersonFieldFilter filter;

    private <T> Path<T> resolvePath(Root<Person> root) {
        String[] fieldSplit = filter.getName().toString().split("\\.");
        if (fieldSplit.length == 2) {
            return root.get(fieldSplit[0]).get(fieldSplit[1]);
        } else {
            return root.get(fieldSplit[0]);
        }
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
        return switch (filter.getName()) {
            case HEIGHT, ID -> {
                Path<Integer> path = resolvePath(root);
                Integer value = Integer.valueOf(filter.getValue());
                yield switch (filter.getOperation()) {
                    case GREATER -> cb.greaterThan(path, value);
                    case LESS -> cb.lessThan(path, value);
                    case GREATER_OR_EQUAL ->
                            cb.greaterThanOrEqualTo(path, value);
                    case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
                    case LIKE, EQUAL -> cb.equal(path, value);
                };
            }
            case CREATION_DATE -> {
                Path<LocalDate> path = resolvePath(root);
                LocalDate value = LocalDate.parse(filter.getValue());
                yield switch (filter.getOperation()) {
                    case GREATER -> cb.greaterThan(path, value);
                    case LESS -> cb.lessThan(path, value);
                    case GREATER_OR_EQUAL ->
                            cb.greaterThanOrEqualTo(path, value);
                    case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
                    case LIKE, EQUAL -> cb.equal(path, value);
                };
            }
            case EYE_COLOR, HAIR_COLOR, NATIONALITY -> {
                Path<String> path = resolvePath(root);
                String value = filter.getValue().toUpperCase().strip();
                yield switch (filter.getOperation()) {
                    case GREATER -> cb.greaterThan(path, value);
                    case LESS -> cb.lessThan(path, value);
                    case GREATER_OR_EQUAL ->
                            cb.greaterThanOrEqualTo(path, value);
                    case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
                    case LIKE -> cb.like(path, "%" + value + "%");
                    case EQUAL -> cb.equal(path, value);
                };
            }
            case COORDINATES_X, LOCATION_Y -> {
                Path<Double> path = resolvePath(root);
                Double value = Double.valueOf(filter.getValue());
                yield switch (filter.getOperation()) {
                    case GREATER -> cb.greaterThan(path, value);
                    case LESS -> cb.lessThan(path, value);
                    case GREATER_OR_EQUAL ->
                            cb.greaterThanOrEqualTo(path, value);
                    case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
                    case LIKE, EQUAL -> cb.equal(path, value);
                };
            }
            case COORDINATES_Y -> {
                Path<Long> path = resolvePath(root);
                Long value = Long.valueOf(filter.getValue());
                yield switch (filter.getOperation()) {
                    case GREATER -> cb.greaterThan(path, value);
                    case LESS -> cb.lessThan(path, value);
                    case GREATER_OR_EQUAL ->
                            cb.greaterThanOrEqualTo(path, value);
                    case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
                    case LIKE, EQUAL -> cb.equal(path, value);
                };
            }
            case LOCATION_X -> {
                Path<Float> path = resolvePath(root);
                Float value = Float.valueOf(filter.getValue());
                yield switch (filter.getOperation()) {
                    case GREATER -> cb.greaterThan(path, value);
                    case LESS -> cb.lessThan(path, value);
                    case GREATER_OR_EQUAL ->
                            cb.greaterThanOrEqualTo(path, value);
                    case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
                    case LIKE, EQUAL -> cb.equal(path, value);
                };
            }
            default -> {
                Path<String> path = resolvePath(root);
                String value = filter.getValue();
                yield switch (filter.getOperation()) {
                    case GREATER -> cb.greaterThan(path, value);
                    case LESS -> cb.lessThan(path, value);
                    case GREATER_OR_EQUAL -> cb.greaterThanOrEqualTo(path, value);
                    case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
                    case LIKE -> cb.like(path, "%" + value + "%");
                    case EQUAL -> cb.equal(path, value);
                };
            }
        };
    }
}
