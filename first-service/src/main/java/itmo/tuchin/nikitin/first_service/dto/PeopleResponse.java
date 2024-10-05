package itmo.tuchin.nikitin.first_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PeopleResponse {
    private List<PersonResponse> data;
    private int total;
}
