package itmo.tuchin.nikitin.first_service.specification;

import itmo.tuchin.nikitin.first_service.entity.Person;
import itmo.tuchin.nikitin.first_service.exceptions.InvalidFilterParameterException;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonFilter {

    public static Specification<Person> filterBy(Map<String, String> filterMap) {
        List<Specification<Person>> specificationList = new ArrayList<>();
        filterMap.forEach((k, v) -> {
            if (v == null || v.isEmpty()) {
                throw new InvalidFilterParameterException(k);
            }
            Pattern pattern = Pattern.compile("^(|>|<|>=|<=|~|=)([\\w-]+)$");
            Matcher matcher = pattern.matcher(v);
            if (!matcher.find()) {
                throw new InvalidFilterParameterException(k);
            }
            String operation = Objects.equals(matcher.group(1), "") ? "=" : matcher.group(1);
            String value = matcher.group(2);
            specificationList.add(
                    new PersonSpecification(
                            new PersonFieldFilter(
                                    Fields.findByString(k),
                                    Operations.findByString(operation),
                                    value
                            )
                    )
            );
        });
        return Specification.allOf(specificationList);
    }
}

