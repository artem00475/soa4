package itmo.tuchin.nikitin.first_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationUpdateDTO {
    private float x;

    private double y;

    private String name;
}
