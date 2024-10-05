package itmo.tuchin.nikitin.first_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    private float x;

    private double y;

    @Column(nullable = false)
    private String name; //Строка не может быть пустой, Поле может быть null
}
