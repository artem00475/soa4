package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.validation.constraint.ColorEnum;
import itmo.tuchin.nikitin.first_service.validation.constraint.CountryEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PersonUpdateDTO {

    private String name;

    @Valid
    private CoordinatesUpdateDTO coordinates;

    @Positive
    @Max(Integer.MAX_VALUE)
    private String height;

    @ColorEnum
    private String eyeColor;

    @ColorEnum
    private String hairColor;

    @CountryEnum
    private String nationality;

    @Valid
    private LocationUpdateDTO location;
}
