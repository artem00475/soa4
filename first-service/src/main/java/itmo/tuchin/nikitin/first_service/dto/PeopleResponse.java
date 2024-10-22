package itmo.tuchin.nikitin.first_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleResponse {
    private List<PersonResponse> data;
    private int total;
}
