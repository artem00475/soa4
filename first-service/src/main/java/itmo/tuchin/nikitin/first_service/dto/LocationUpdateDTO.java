package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.validation.constraint.NullOrNotEmpty;
import itmo.tuchin.nikitin.first_service.validation.constraint.Precision;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationUpdateDTO {
    @DecimalMax("3.4028235e+38")
    @DecimalMin("-3.4028235e+38")
    @Precision(precision = 7)
    private String x;

    @DecimalMax(value = "1.7976931348623157e+308")
    @DecimalMin(value = "-1.7976931348623157e+308")
    @Precision(precision = 16)
    private String y;

    @NullOrNotEmpty
    private String name;
}
