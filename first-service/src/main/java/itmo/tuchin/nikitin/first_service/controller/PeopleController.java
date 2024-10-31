package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.dto.*;
import itmo.tuchin.nikitin.first_service.exceptions.InvalidFilterException;
import itmo.tuchin.nikitin.first_service.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
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
import java.util.Map;
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
            @Valid @Positive @Max(Integer.MAX_VALUE) @RequestParam(required = false, defaultValue = "10") int limit,
            @Valid @PositiveOrZero @Max(Integer.MAX_VALUE) @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false) Map<String, String> map
    ) {
        return ResponseEntity.ok(personService.getAll(limit, offset, map));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PersonResponse> getPerson(@Valid @PathVariable int id) {
        return ResponseEntity.ok(personService.get(id));
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
        return ResponseEntity.ok(personService.add(personDTO));
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> invalidSort() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Person not found"));
    }
}


