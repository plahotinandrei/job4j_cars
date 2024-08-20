package ru.job4j.cars.model;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id", foreignKey = @ForeignKey(name = "ENGINE_ID_FK"))
    private Engine engine;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car", cascade = CascadeType.REMOVE)
    private List<HistoryOwner> history = new ArrayList<>();
}
