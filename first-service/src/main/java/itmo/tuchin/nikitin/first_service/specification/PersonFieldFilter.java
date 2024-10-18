package itmo.tuchin.nikitin.first_service.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonFieldFilter {
    private Fields name;
    private Operations operation;
    private String value;
}
