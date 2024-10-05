package itmo.tuchin.nikitin.first_service.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class SortDTO {

    private String id;

    private String creationDate;

    private String name;

    private String coordinates_x;

    private String coordinates_y;

    private String height;

    private String eyeColor;

    private String hairColor;

    private String nationality;

    private String locationX;

    private String locationY;

    private String locationName;
}
