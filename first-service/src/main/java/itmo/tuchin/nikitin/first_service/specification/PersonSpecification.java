package itmo.tuchin.nikitin.first_service.specification;

import itmo.tuchin.nikitin.first_service.entity.Person;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

@Data
public abstract class PersonSpecification<E> implements Specification<Person> {

    private Fields name;
    private Operations operation;
    private E value;

    protected <T> Path<T> resolvePath(Root<Person> root) {
        String[] fieldSplit = name.toString().split("\\.");
        if (fieldSplit.length == 2) {
            return root.get(fieldSplit[0]).get(fieldSplit[1]);
        } else {
            return root.get(fieldSplit[0]);
        }
    }

    protected Predicate getPredicate(Path<String> path, CriteriaBuilder cb, String value) {
        return switch (operation) {
            case GREATER -> cb.greaterThan(path, value);
            case LESS -> cb.lessThan(path, value);
            case GREATER_OR_EQUAL -> cb.greaterThanOrEqualTo(path, value);
            case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
            case LIKE -> cb.like(path, "%" + value + "%");
            default -> cb.equal(path, value);
        };
    }

    protected <T extends Comparable<T>> Predicate getPredicate(Path<T> path, CriteriaBuilder cb, T value) {
        return switch (operation) {
            case GREATER -> cb.greaterThan(path, value);
            case LESS -> cb.lessThan(path, value);
            case GREATER_OR_EQUAL -> cb.greaterThanOrEqualTo(path, value);
            case LESS_OR_EQUAL -> cb.lessThanOrEqualTo(path, value);
            default -> cb.equal(path, value);
        };
    }

    @Override
    public abstract Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb);
}
