package itmo.tuchin.nikitin.first_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoordinatesUpdateDTO {
    @Max(682)
    private Double x; //Максимальное значение поля: 682, Поле не может быть null

    @Min(-984)
    private long y; //Значение поля должно быть больше -985
}
