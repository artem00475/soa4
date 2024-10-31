package itmo.tuchin.nikitin.first_service.specification.specifications;

import itmo.tuchin.nikitin.first_service.entity.Person;
import itmo.tuchin.nikitin.first_service.specification.Fields;
import itmo.tuchin.nikitin.first_service.specification.Operations;
import itmo.tuchin.nikitin.first_service.specification.PersonSpecification;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

public class PersonSpecificationString extends PersonSpecification<String> {

    public PersonSpecificationString(
            @NotNull Fields field,
            @NotNull Operations operation,
            @NotEmpty String value
    ) {
        this.setOperation(operation);
        this.setName(field);
        this.setValue(value);
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
        Path<String> path = resolvePath(root);
        return getPredicate(path, cb, getValue());
    }
}
