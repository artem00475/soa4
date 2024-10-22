package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.dto.*;
import itmo.tuchin.nikitin.first_service.exceptions.InvalidFilterException;
import itmo.tuchin.nikitin.first_service.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(
        path = "/people",
        produces = MediaType.APPLICATION_JSON_VALUE + "; charset=utf-8"
)
@CrossOrigin(origins = "https://se.ifmo.ru")
@Validated
public class PeopleController {
    private PersonService personService;

    @GetMapping
    public ResponseEntity<PeopleResponse> getPeople(
            @Valid @Positive @RequestParam(required = false, defaultValue = "10") int limit,
            @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false) Map<String, String> map
    ) {
        List<String> errors = new LinkedList<>();
        Map<String, Sort.Direction> sort = new HashMap<>();
        Map<String, String> filter = new HashMap<>();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^sort\\[(.+)\\]$");
        map.forEach((k, v) -> {
            Matcher matcher = pattern.matcher(k);
            if (matcher.find()) {
                v = v.toUpperCase().trim();
                if (v.equals("ASC") | v.equals("DESC")) {
                    sort.put(matcher.group(1), Sort.Direction.valueOf(v));
                } else {
                    errors.add(k);
                }
            } else if (!k.equals("limit") && !k.equals("offset")){
                filter.put(k, v);
            }
        });
        if (!errors.isEmpty()) {
            throw new InvalidFilterException("Not valid parameters:" + String.join(",", errors));
        }
        return ResponseEntity.ok(personService.getAll(limit, offset, sort, filter));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PersonResponse> getPerson(@Valid @PathVariable int id) {
        PersonResponse person = personService.get(id);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Null> deletePerson(@Valid @PathVariable int id) {
        personService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Null> patchPerson(@Valid @PathVariable int id, @Valid @RequestBody PersonUpdateDTO personDTO) {
        personService.patch(id, personDTO);
        return ResponseEntity.status(204).build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Null> putPerson(@Valid @PathVariable int id, @Valid @RequestBody PersonDTO personDTO) {
        personService.update(id, personDTO);
        return ResponseEntity.status(204).build();
    }

    @PostMapping
    public ResponseEntity<PersonResponse> addPerson(@Valid @RequestBody PersonDTO personDTO) {
        PersonResponse person = personService.add(personDTO);
        return ResponseEntity.ok(person);
    }

    @GetMapping("/{function}/height")
    public ResponseEntity<AggregateDTO> aggregateHeight(
            @Valid @PathVariable @Pattern(regexp = "count|average") String function,
            @Valid @RequestParam(required = false) Integer height
    ) {
        AggregateDTO aggregateDTO = new AggregateDTO(switch (function) {
            case "count" -> personService.countHeight(height);
            case "average" -> personService.averageHeight();
            default -> "0";
        });
        return ResponseEntity.ok(aggregateDTO);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> validateParameters(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Not valid parameters:" + ex.getName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> validateBody(MethodArgumentNotValidException ex) {
        String message = ex.getFieldErrors().stream().map((FieldError::getField)).distinct().collect(Collectors.joining(","));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Not valid parameters:" + message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> parseJSON() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("JSON parse error"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> validateBody(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream().map((cv -> cv.getPropertyPath().toString().split("\\.")[1])).distinct().collect(Collectors.joining(","));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Not valid parameters:" + message));
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<ErrorMessage> invalidFilter(InvalidFilterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorMessage> invalidSort(PropertyReferenceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Not valid parameters:sort[" + ex.getPropertyName() + "]"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> invalidSort() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Person not found"));
    }
}


