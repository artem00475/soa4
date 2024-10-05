package itmo.tuchin.nikitin.first_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDTO {
    private float x;

    private double y;

    @NotNull
    @NotEmpty
    private String name; //Строка не может быть пустой, Поле может быть null
}
