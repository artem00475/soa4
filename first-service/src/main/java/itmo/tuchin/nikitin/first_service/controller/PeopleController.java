package itmo.tuchin.nikitin.first_service.controller;

import itmo.tuchin.nikitin.first_service.dto.*;
import itmo.tuchin.nikitin.first_service.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.orm.jpa.JpaSystemException;
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
    public ResponseEntity getPeople(
            @Valid @Positive @RequestParam(required = false, defaultValue = "10") int limit,
            @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false) Map<String, String> map
    ) {
        try {
            List<String> errors = new LinkedList<>();
            Map<String, Sort.Direction> sort = new HashMap<>();
            Map<String, String> filter = new HashMap<>();
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^sort\\[(.+)\\]$");
            map.forEach((k, v) -> {
                Matcher matcher = pattern.matcher(k);
                if (matcher.find()) {
                    try {
                        v = v.toUpperCase().strip();
                        sort.put(matcher.group(1), Sort.Direction.valueOf(v));
                    } catch (IllegalArgumentException ex) {
                        errors.add(k);
                    }
                } else if (!k.equals("limit") && !k.equals("offset")){
                    filter.put(k, v);
                }
            });
            System.out.println(filter);
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorMessage("Not valid parameters:" + String.join(",", errors)));
            }
            return ResponseEntity.ok(personService.getAll(limit, offset, sort, filter));
        } catch (PropertyReferenceException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Not valid parameters:sort[" + ex.getPropertyName() + "]"));
        } catch (InvalidDataAccessApiUsageException | JpaSystemException ex) {
            return ResponseEntity.badRequest().body(new ErrorMessage("Not valid parameters:filter"));
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getPerson(@Valid @PathVariable int id) {
        try {
            PersonResponse person = personService.get(id);
            return ResponseEntity.ok(person);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Person not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage("Error finding person"));
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deletePerson(@Valid @PathVariable int id) {
        try {
            personService.delete(id);
            return ResponseEntity.status(204).build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Person not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage("Error deleting person"));
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity patchPerson(@Valid @PathVariable int id, @Valid @RequestBody PersonUpdateDTO personDTO) {
        try {
            personService.patch(id, personDTO);
            return ResponseEntity.status(204).build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Person not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage("Error patching person"));
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity putPerson(@Valid @PathVariable int id, @Valid @RequestBody PersonDTO personDTO) {
        try {
            personService.update(id, personDTO);
            return ResponseEntity.status(204).build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("Person not found"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage("Error updating person"));
        }
    }

    @PostMapping
    public ResponseEntity addPerson(@Valid @RequestBody PersonDTO personDTO) {
        try {
            PersonResponse person = personService.add(personDTO);
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorMessage("Error creating person"));
        }
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
        String message = ex.getFieldErrors().stream().map((FieldError::getField)).collect(Collectors.joining(","));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Not valid parameters:" + message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> parseJSON(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("JSON parse error"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> validateBody(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream().map((cv -> cv.getPropertyPath().toString().split("\\.")[1])).collect(Collectors.joining(","));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessage("Not valid parameters:" + message));
    }
}


