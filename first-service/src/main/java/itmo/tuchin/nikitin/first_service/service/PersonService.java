package itmo.tuchin.nikitin.first_service.service;

import itmo.tuchin.nikitin.first_service.dto.*;
import itmo.tuchin.nikitin.first_service.entity.*;
import itmo.tuchin.nikitin.first_service.exceptions.InvalidFilterException;
import itmo.tuchin.nikitin.first_service.repository.CoordinatesRepository;
import itmo.tuchin.nikitin.first_service.repository.LocationRepository;
import itmo.tuchin.nikitin.first_service.repository.PersonRepository;
import itmo.tuchin.nikitin.first_service.specification.Fields;
import itmo.tuchin.nikitin.first_service.specification.PersonFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class PersonService {

    private final PersonRepository personRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    private final Pattern sortPattern = Pattern.compile("^sort\\[(.+)\\]$");

    private Pair<String, Sort.Direction> parseSortParam(String k, String v) {
        Fields.findByString(k);
        v = v.toUpperCase().trim();
        return Pair.of(k, Sort.Direction.valueOf(v));
    }

    public PeopleResponse getAll(int limit, int offset, Map<String, String> paramsMap) {
        List<String> errors = new LinkedList<>();
        List<Sort.Order> sortList = new LinkedList<>();
        List<Specification<Person>> specificationList = new LinkedList<>();
        paramsMap.forEach((k, v) -> {
            try {
                Matcher isSortParam = sortPattern.matcher(k);
                if (isSortParam.find()) {
                    Pair<String, Sort.Direction> sortParam = parseSortParam(isSortParam.group(1), v);
                    sortList.add(Sort.Order.by(sortParam.getLeft()).with(sortParam.getRight()));
                } else if (!k.equals("limit") && !k.equals("offset")) {
                    specificationList.add(PersonFilter.getPersonSpecificationForField(k, v));
                }
            } catch (Throwable e) {
                errors.add(k);
            }
        });
        if (!errors.isEmpty()) {
            throw new InvalidFilterException("Not valid parameters:" + errors.stream().distinct().collect(Collectors.joining(",")));
        }
        Page<Person> personPage = personRepository.findAll(PersonFilter.filterBy(specificationList), PageRequest.of(offset, limit, Sort.by(sortList)));
        List<PersonResponse> personResponses = new ArrayList<>();
        personPage.get().forEach(person -> personResponses.add(getResponse(person)));
        return new PeopleResponse(personResponses, (int) personPage.getTotalElements());
    }

    private PersonResponse getResponse(@NotNull Person person) {
        return modelMapper.map(person, PersonResponse.class);
    }

    public PersonResponse get(int id) {
        Optional<Person> person = personRepository.findPersonById(id);
        if (person.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return getResponse(person.get());
    }

    public PersonResponse add(PersonDTO personDTO) {
        Person person = modelMapper.map(personDTO, Person.class);
        person.setCreationDate(LocalDate.now());
        person.setCoordinates(coordinatesRepository.save(person.getCoordinates()));
        if (person.getLocation() != null) {
            person.setLocation(locationRepository.save(person.getLocation()));
        }
        return getResponse(personRepository.save(person));
    }

    public void delete(int id) {
        Optional<Person> person = personRepository.findPersonById(id);
        if (person.isEmpty()) {
            throw new EntityNotFoundException();
        }
        personRepository.delete(person.get());
        coordinatesRepository.delete(person.get().getCoordinates());
        if (person.get().getLocation() != null) {
            locationRepository.delete(person.get().getLocation());
        }
    }

    public void patch(int id, PersonUpdateDTO personUpdateDTO) {
        Optional<Person> optionalPerson = personRepository.findPersonById(id);
        if (optionalPerson.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Person person = optionalPerson.get();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(personUpdateDTO, person);
        modelMapper.getConfiguration().setSkipNullEnabled(false);
        personRepository.save(person);
    }

    public void update(int id, PersonDTO personDTO) {
        Optional<Person> personOptional = personRepository.findPersonById(id);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Person person = personOptional.get();
        modelMapper.map(personDTO, person);
        personRepository.save(person);
    }

    public String countHeight(@Valid @NotNull @Positive Integer height) {
        return personRepository.countTotalByHeight(height).toString();
    }

    public String averageHeight() {
        return personRepository.getAverageHeight().toString();
    }
}
