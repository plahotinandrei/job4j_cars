package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

class HbmCarRepositoryTest {

    private static StandardServiceRegistry registry;

    private static CrudRepository crudRepository;

    private static CarRepository carRepository;

    @BeforeAll
    public static void init() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        carRepository = new HbmCarRepository(crudRepository);
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @AfterEach
    public void wipeTables() {
        crudRepository.run("delete from Car", Map.of());
    }

    @Test
    public void whenCreateCarThenReturnCar() {
        Model model = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car = new Car(0, "Granta", engine, model, new ArrayList<>());
        Optional<Car> carOptional = carRepository.create(car);
        assertThat(carOptional.get()).isEqualTo(car);
    }

    @Test
    public void whenFindAll() {
        Model model = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car1 = new Car(0, "Granta", engine, model, new ArrayList<>());
        Car car2 = new Car(0, "Priora", engine, model, new ArrayList<>());
        carRepository.create(car1);
        carRepository.create(car2);
        assertThat(carRepository.findAll())
                .usingRecursiveComparison()
                .ignoringFields("history")
                .isEqualTo(List.of(car1, car2));
    }

    @Test
    public void whenFindAllThenEmptyList() {
        assertThat(carRepository.findAll()).isEmpty();
    }

    @Test
    public void whenFindById() {
        Model model = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car = new Car(0, "Granta", engine, model, new ArrayList<>());
        carRepository.create(car);
        Optional<Car> carOptional = carRepository.findById(car.getId());
        assertThat(carOptional.get())
                .usingRecursiveComparison()
                .ignoringFields("history")
                .isEqualTo(car);
    }

    @Test
    public void whenUpdateThenUpdatedCar() {
        Model model = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car = new Car(0, "Granta", engine, model, new ArrayList<>());
        carRepository.create(car);
        Model updatedModel = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Reno")).get();
        Engine updatedEngine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Электрический")).get();
        Car updatedCar = new Car(car.getId(), "Twizy", updatedEngine, updatedModel, new ArrayList<>());
        assertThat(carRepository.update(updatedCar)).isTrue();
        Optional<Car> carOptional = carRepository.findById(car.getId());
        assertThat(carOptional.get())
                .usingRecursiveComparison()
                .ignoringFields("history")
                .isEqualTo(updatedCar);
    }

    @Test
    public void whenDeleteThenTrue() {
        Model model = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car = new Car(0, "Granta", engine, model, new ArrayList<>());
        carRepository.create(car);
        assertThat(carRepository.delete(car.getId())).isTrue();
    }

    @Test
    public void whenDeleteThenFalse() {
        Model model = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car = new Car(0, "Granta", engine, model, new ArrayList<>());
        carRepository.create(car);
        assertThat(carRepository.delete(car.getId() + 1)).isFalse();
    }

}