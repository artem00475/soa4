package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.validation.constraint.NullOrNotEmpty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    @DecimalMax("3.4028235e+38")
    @DecimalMin("-3.4028235e+38")
    private String x;

    @DecimalMax(value = "1.7976931348623157e+308")
    @DecimalMin(value = "-1.7976931348623157e+308")
    private String y;

    @NullOrNotEmpty
    private String name; //Строка не может быть пустой, Поле может быть null
}
