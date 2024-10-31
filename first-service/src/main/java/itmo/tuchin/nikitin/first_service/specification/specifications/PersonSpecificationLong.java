package itmo.tuchin.nikitin.first_service.specification.specifications;

import itmo.tuchin.nikitin.first_service.entity.Person;
import itmo.tuchin.nikitin.first_service.specification.Fields;
import itmo.tuchin.nikitin.first_service.specification.Operations;
import itmo.tuchin.nikitin.first_service.specification.PersonSpecification;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

public class PersonSpecificationLong extends PersonSpecification<Long> {

    public PersonSpecificationLong(
            @NotNull Fields field,
            @NotNull Operations operation,
            @NotEmpty
            @Max(Long.MAX_VALUE)
            @Min(Long.MIN_VALUE)
            String value
    ) {
        this.setOperation(operation);
        this.setName(field);
        this.setValue(Long.parseLong(value));
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
        Path<Long> path = resolvePath(root);
        return getPredicate(path, cb, getValue());
    }
}
