package itmo.tuchin.nikitin.first_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @OneToOne(fetch = FetchType.EAGER)
    private Coordinates coordinates; //Поле не может быть null

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private java.time.LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Column(nullable = false)
    @Positive
    private Integer height; //Поле не может быть null, Значение поля должно быть больше 0

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Color eyeColor; //Поле не может быть null

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Color hairColor; //Поле не может быть null

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Country nationality; //Поле может быть null

    @OneToOne(fetch = FetchType.EAGER)
    private Location location; //Поле может быть null
}
