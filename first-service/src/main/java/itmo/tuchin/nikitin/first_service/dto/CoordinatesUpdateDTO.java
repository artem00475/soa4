package itmo.tuchin.nikitin.first_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoordinatesUpdateDTO {
    @DecimalMax(value = "682.0")
    @DecimalMin(value = "-1.7976931348623157e+308")
    private String x; //Максимальное значение поля: 682, Поле не может быть null

    @Min(-984)
    @Max(Integer.MAX_VALUE)
    private String y; //Значение поля должно быть больше -985
}
