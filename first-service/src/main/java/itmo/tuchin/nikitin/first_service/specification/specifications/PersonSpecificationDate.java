package itmo.tuchin.nikitin.first_service.specification.specifications;

import itmo.tuchin.nikitin.first_service.entity.Person;
import itmo.tuchin.nikitin.first_service.specification.Fields;
import itmo.tuchin.nikitin.first_service.specification.Operations;
import itmo.tuchin.nikitin.first_service.specification.PersonSpecification;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotEmpty;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PersonSpecificationDate extends PersonSpecification<LocalDate> {

    public PersonSpecificationDate(
            @NotNull Fields field,
            @NotNull Operations operation,
            @NotEmpty String value
    ) {
        this.setOperation(operation);
        this.setName(field);
        this.setValue(LocalDate.parse(value));
    }

    @Override
    public Predicate toPredicate(@NotNull Root<Person> root, CriteriaQuery<?> query, @NotNull CriteriaBuilder cb) {
        Path<Date> path = resolvePath(root);
        Date value = Date.from(getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return getPredicate(path, cb, value);
    }
}
