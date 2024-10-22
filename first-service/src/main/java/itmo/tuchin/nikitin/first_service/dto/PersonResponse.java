package itmo.tuchin.nikitin.first_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {
    private int id;
    private String name;
    private CoordinatesDTO coordinates;
    private String creationDate;
    private Integer height;
    private String eyeColor;
    private String hairColor;
    private String nationality;
    private LocationDTO location;
}
