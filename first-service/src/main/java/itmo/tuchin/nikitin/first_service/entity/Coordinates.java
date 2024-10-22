package itmo.tuchin.nikitin.first_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(nullable = false)
    @Max(682)
    private Double x; //Максимальное значение поля: 682, Поле не может быть null

    @Min(-984)
    private long y; //Значение поля должно быть больше -985
}
