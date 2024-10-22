package itmo.tuchin.nikitin.first_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    private float x;

    private double y;

    @Column(columnDefinition = "TEXT")
    private String name; //Строка не может быть пустой, Поле может быть null
}
