package ru.job4j.cars.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmCarRepository implements CarRepository {

    private final CrudRepository crudRepository;

    private final static Logger LOG = LoggerFactory.getLogger(HbmCarRepository.class.getName());

    @Override
    public Optional<Car> create(Car car) {
        Optional<Car> carOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(car));
            carOptional = Optional.of(car);
        } catch (Exception e) {
            LOG.error("Failed to create car", e);
        }
        return carOptional;
    }

    @Override
    public List<Car> findAll() {
        return crudRepository.query("from Car c JOIN FETCH c.engine LEFT JOIN FETCH c.owners", Car.class);
    }

    @Override
    public Optional<Car> findById(int id) {
        return crudRepository.optional(
                "from Car c JOIN FETCH c.engine where c.id=:id",
                Car.class, Map.of("id", id)
        );
    }

    @Override
    public boolean update(Car car) {
        boolean rsl = false;
        try {
            crudRepository.run(session -> session.update(car));
            rsl = true;
        } catch (Exception e) {
            LOG.error("Failed to update car");
        }
        return rsl;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        Optional<Car> carOptional = crudRepository.optional(
                "from Car c LEFT JOIN FETCH c.owners where c.id=:id",
                Car.class, Map.of("id", id)
        );
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.getOwners().clear();
            try {
                crudRepository.run(session -> session.remove(car));
                rsl = true;
            } catch (Exception e) {
                LOG.error("Failed to remove car", e);
            }
        }
        return rsl;
    }
}
