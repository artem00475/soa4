package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.validation.constraint.Precision;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordinatesDTO {

    @DecimalMax(value = "682.0")
    @DecimalMin(value = "-1.7976931348623157e+308")
    @Precision(precision = 16)
    @NotNull
    private String x; //Максимальное значение поля: 682, Поле не может быть null

    @Min(-984)
    @Max(Long.MAX_VALUE)
    private String y; //Значение поля должно быть больше -985
}
