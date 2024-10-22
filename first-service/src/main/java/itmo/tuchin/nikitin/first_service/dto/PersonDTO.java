package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.validation.constraint.ColorEnum;
import itmo.tuchin.nikitin.first_service.validation.constraint.CountryEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PersonDTO {

    @NotNull
    @NotEmpty
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    @Valid
    private CoordinatesDTO coordinates; //Поле не может быть null

    @NotNull
    @Positive
    @Max(Integer.MAX_VALUE)
    private String height; //Поле не может быть null, Значение поля должно быть больше 0

    @NotEmpty
    @ColorEnum
    private String eyeColor; //Поле не может быть null

    @NotEmpty
    @ColorEnum
    private String hairColor; //Поле не может быть null

    @CountryEnum
    private String nationality; //Поле может быть null

    @Valid
    private LocationDTO location; //Поле может быть null
}
