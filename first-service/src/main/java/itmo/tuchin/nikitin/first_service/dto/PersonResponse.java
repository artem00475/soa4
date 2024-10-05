package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.entity.Color;
import itmo.tuchin.nikitin.first_service.entity.Coordinates;
import itmo.tuchin.nikitin.first_service.entity.Country;
import itmo.tuchin.nikitin.first_service.entity.Location;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
public class PersonResponse {
    private int id;
    private String name;
    private CoordinatesDTO coordinates;
    private String creationDate;
    private Integer height;
    private String eyeColor;
    private String hairColor;
    private String nationality;
    private LocationDTO location;
}
