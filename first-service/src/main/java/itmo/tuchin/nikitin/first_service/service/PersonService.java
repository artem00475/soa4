package itmo.tuchin.nikitin.first_service.service;

import itmo.tuchin.nikitin.first_service.dto.*;
import itmo.tuchin.nikitin.first_service.entity.*;
import itmo.tuchin.nikitin.first_service.repository.CoordinatesRepository;
import itmo.tuchin.nikitin.first_service.repository.LocationRepository;
import itmo.tuchin.nikitin.first_service.repository.PersonRepository;
import itmo.tuchin.nikitin.first_service.specification.PersonFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private Person setPersonFields(Person person, PersonDTO personDTO, LocalDate date, Coordinates coordinates, Location location) {
        person.setCreationDate(date);
        person.setName(personDTO.getName());
        person.setHeight(personDTO.getHeight());
        person.setHairColor(Color.valueOf(personDTO.getHairColor()));
        person.setEyeColor(Color.valueOf(personDTO.getEyeColor()));
        person.setNationality(Country.valueOf(personDTO.getNationality()));
        coordinates.setX(personDTO.getCoordinates().getX());
        coordinates.setY(personDTO.getCoordinates().getY());
        coordinates = coordinatesRepository.save(coordinates);
        person.setCoordinates(coordinates);
        location.setName(personDTO.getLocation().getName());
        location.setX(personDTO.getLocation().getX());
        location.setY(personDTO.getLocation().getY());
        location = locationRepository.save(location);
        person.setLocation(location);
        return person;
    }

    public PeopleResponse getAll(int limit, int offset, Map<String, Sort.Direction> sort, Map<String, String> filter) {
        List<Sort.Order> sortList = new ArrayList<>();
        sort.forEach((k, v) -> sortList.add(Sort.Order.by(k).with(v)));
        Page<Person> personPage = personRepository.findAll(PersonFilter.filterBy(filter),PageRequest.of(offset, limit, Sort.by(sortList)));
        List<PersonResponse> personResponses = new ArrayList<>();
        personPage.get().forEach(person -> personResponses.add(getResponse(person)));
        return new PeopleResponse(personResponses, (int) personPage.getTotalElements());
    }

    public PersonResponse add(PersonDTO personDTO) {
        Person person = new Person();
        person = personRepository.save(setPersonFields(person, personDTO, LocalDate.now(), new Coordinates(), new Location()));
        return getResponse(person);
    }

    private PersonResponse getResponse(@NotNull Person person) {
        return new PersonResponse(
                person.getId(),
                person.getName(),
                new CoordinatesDTO(person.getCoordinates().getX(), person.getCoordinates().getY()),
                person.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                person.getHeight(),
                person.getEyeColor().name(),
                person.getHairColor().name(),
                person.getNationality().name(),
                new LocationDTO(person.getLocation().getX(), person.getLocation().getY(), person.getLocation().getName())
        );
    }

    public PersonResponse get(int id) {
        Optional<Person> person = personRepository.findPersonById(id);
        if (person.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return getResponse(person.get());
    }

    public void delete(int id) {
        Optional<Person> person = personRepository.findPersonById(id);
        if (person.isEmpty()) {
            throw new EntityNotFoundException();
        }
        personRepository.delete(person.get());
    }

    public void patch(int id, PersonUpdateDTO personUpdateDTO) {
        Optional<Person> optionalPerson = personRepository.findPersonById(id);
        if (optionalPerson.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Person person = optionalPerson.get();
        if (personUpdateDTO.getName() != null && !personUpdateDTO.getName().isEmpty()) {
            person.setName(personUpdateDTO.getName());
        }
        if (personUpdateDTO.getEyeColor() != null) {
            person.setEyeColor(Color.valueOf(personUpdateDTO.getEyeColor()));
        }
        if (personUpdateDTO.getHairColor() != null) {
            person.setHairColor(Color.valueOf(personUpdateDTO.getHairColor()));
        }
        if (personUpdateDTO.getNationality() != null) {
            person.setNationality(Country.valueOf(personUpdateDTO.getNationality()));
        }
        if (personUpdateDTO.getHeight() != null) {
            person.setHeight(personUpdateDTO.getHeight());
        }
        CoordinatesUpdateDTO coordinatesDTO = personUpdateDTO.getCoordinates();
        if (coordinatesDTO != null) {
            Coordinates coordinates = person.getCoordinates();
            if (!coordinatesDTO.getX().isNaN()) {
                coordinates.setX(coordinatesDTO.getX());
            }
            if (coordinatesDTO.getY() != 0L) {
                coordinates.setY(coordinatesDTO.getY());
            }
            coordinates = coordinatesRepository.save(coordinates);
            person.setCoordinates(coordinates);
        }
        LocationUpdateDTO locationDTO = personUpdateDTO.getLocation();
        if (locationDTO != null) {
            Location location = person.getLocation();
            if (!Float.isNaN(locationDTO.getX())) {
                location.setX(locationDTO.getX());
            }
            if (!Double.isNaN(locationDTO.getY())) {
                location.setY(locationDTO.getY());
            }
            if (locationDTO.getName() != null && !locationDTO.getName().isEmpty()) {
                location.setName(locationDTO.getName());
            }
            location = locationRepository.save(location);
            person.setLocation(location);
        }
        personRepository.save(person);
    }

    public void update(int id, PersonDTO personDTO) {
        Optional<Person> personOptional = personRepository.findPersonById(id);
        if (personOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }
        Person person = personOptional.get();
        personRepository.save(setPersonFields(person, personDTO, person.getCreationDate(), person.getCoordinates(), person.getLocation()));
    }

    public String countHeight(@Valid @NotNull @Positive Integer height) {
        return personRepository.countTotalByHeight(height).toString();
    }

    public String averageHeight() {
        return personRepository.getAverageHeight().toString();
    }
}
