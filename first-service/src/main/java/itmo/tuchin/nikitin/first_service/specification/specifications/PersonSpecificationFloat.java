package itmo.tuchin.nikitin.first_service.specification.specifications;

import itmo.tuchin.nikitin.first_service.entity.Person;
import itmo.tuchin.nikitin.first_service.specification.Fields;
import itmo.tuchin.nikitin.first_service.specification.Operations;
import itmo.tuchin.nikitin.first_service.specification.PersonSpecification;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

public class PersonSpecificationFloat extends PersonSpecification<Float> {

    public PersonSpecificationFloat(
            @NotNull Fields field,
            @NotNull Operations operation,
            @NotEmpty
            @DecimalMax("3.4028235e+38")
            @DecimalMin("-3.4028235e+38")
            String value
    ) {
        this.setOperation(operation);
        this.setName(field);
        this.setValue(Float.parseFloat(value));
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
        Path<Float> path = resolvePath(root);
        return getPredicate(path, cb, getValue());
    }
}
