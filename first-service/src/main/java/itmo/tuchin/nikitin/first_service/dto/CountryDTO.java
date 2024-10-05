package itmo.tuchin.nikitin.first_service.dto;

import itmo.tuchin.nikitin.first_service.entity.Country;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class CountryDTO {
    private List<Country> data = Arrays.stream(Country.values()).toList();
}
