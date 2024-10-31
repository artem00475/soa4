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

public class PersonSpecificationDouble extends PersonSpecification<Double> {

    public PersonSpecificationDouble(
            @NotNull Fields field,
            @NotNull Operations operation,
            @NotEmpty
            @DecimalMax(value = "1.7976931348623157e+308")
            @DecimalMin(value = "-1.7976931348623157e+308")
            String value
    ) {
        this.setOperation(operation);
        this.setName(field);
        this.setValue(Double.parseDouble(value));
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
        Path<Double> path = resolvePath(root);
        return getPredicate(path, cb, getValue());
    }
}
