package itmo.tuchin.nikitin.first_service.service;

import itmo.tuchin.nikitin.first_service.dto.*;
import itmo.tuchin.nikitin.first_service.entity.*;
import itmo.tuchin.nikitin.first_service.exceptions.InvalidFilterException;
import itmo.tuchin.nikitin.first_service.repository.CoordinatesRepository;
import itmo.tuchin.nikitin.first_service.repository.LocationRepository;
import itmo.tuchin.nikitin.first_service.repository.PersonRepository;
import itmo.tuchin.nikitin.first_service.specification.PersonFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class PersonService {

    private final PersonRepository personRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    public PeopleResponse getAll(int limit, int offset, Map<String, Sort.Direction> sort, Map<String, String> filter) {
        List<Sort.Order> sortList = new ArrayList<>();
        sort.forEach((k, v) -> sortList.add(Sort.Order.by(k).with(v)));
        try {
            Page<Person> personPage = personRepository.findAll(PersonFilter.filterBy(filter), PageRequest.of(offset, limit, Sort.by(sortList)));
            List<PersonResponse> personResponses = new ArrayList<>();
            personPage.get().forEach(person -> personResponses.add(getResponse(person)));
            return new PeopleResponse(personResponses, (int) personPage.getTotalElements());
        } catch (Exception ex) {
            throw new InvalidFilterException("Not valid parameters:filter");
        }
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
