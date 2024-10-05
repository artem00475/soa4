package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.entity.Color;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ColorDTO {
    private List<Color> data = Arrays.stream(Color.values()).toList();
}
