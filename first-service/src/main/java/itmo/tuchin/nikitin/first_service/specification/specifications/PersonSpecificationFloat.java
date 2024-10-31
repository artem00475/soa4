package itmo.tuchin.nikitin.first_service.specification.specifications;

import itmo.tuchin.nikitin.first_service.entity.Person;
import itmo.tuchin.nikitin.first_service.specification.Fields;
import itmo.tuchin.nikitin.first_service.specification.Operations;
import itmo.tuchin.nikitin.first_service.specification.PersonSpecification;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class PersonSpecificationFloat extends PersonSpecification<Float> {
    public PersonSpecificationFloat(
            @NotNull Fields field,
            @NotNull Operations operation,
            @NotEmpty String value
    ) {
        this.setOperation(operation);
        this.setName(field);
        BigDecimal bigDecimal = new BigDecimal(value);
        if (bigDecimal.precision() > 7) {
            throw new IllegalArgumentException();
        }
        this.setValue(bigDecimal.floatValue());
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
        Path<Float> path = resolvePath(root);
        return getPredicate(path, cb, getValue());
    }
}
