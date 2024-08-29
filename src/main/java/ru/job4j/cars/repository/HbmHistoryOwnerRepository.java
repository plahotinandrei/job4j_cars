package ru.job4j.cars.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.HistoryOwner;
import ru.job4j.cars.model.Owner;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmHistoryOwnerRepository implements HistoryOwnerRepository {

    private final CrudRepository crudRepository;

    private final static Logger LOG = LoggerFactory.getLogger(HbmCarRepository.class.getName());

    @Override
    public Optional<HistoryOwner> create(int carId, int ownerId, LocalDateTime startAt) {
        Optional<HistoryOwner> historyOptional = Optional.empty();
        HistoryOwner history;
        Optional<Car> carOptional = crudRepository.optional(
                "from Car where id=:id",
                Car.class, Map.of("id", carId)
        );
        Optional<Owner> ownerOptional = crudRepository.optional(
                "from Owner where id=:id",
                Owner.class, Map.of("id", ownerId)
        );
        if (carOptional.isPresent() && ownerOptional.isPresent()) {
            history = new HistoryOwner();
            history.setCar(carOptional.get());
            history.setOwner(ownerOptional.get());
            history.setStartAt(startAt);
            try {
                crudRepository.run(session -> session.persist(history));
                historyOptional = Optional.of(history);
            } catch (Exception e) {
                LOG.error("Failed to create history", e);
            }
        }
        return historyOptional;
    }

    @Override
    public List<HistoryOwner> findAll() {
        return crudRepository.query(
                "from HistoryOwner h JOIN FETCH h.car JOIN FETCH h.owner",
                HistoryOwner.class
        );
    }

    @Override
    public List<HistoryOwner> findAllByCarId(int carId) {
        return crudRepository.query(
                "from HistoryOwner h JOIN FETCH h.car c JOIN FETCH h.owner where c.id=:carId",
                HistoryOwner.class, Map.of("carId", carId)
        );
    }

    @Override
    public List<HistoryOwner> findAllByOwnerId(int ownerId) {
        return crudRepository.query(
                "from HistoryOwner h JOIN FETCH h.car JOIN FETCH h.owner o where o.id=:ownerId",
                HistoryOwner.class, Map.of("ownerId", ownerId)
        );
    }

    @Override
    public boolean endOwnership(int id, LocalDateTime endAt) {
        boolean rsl = false;
        Optional<HistoryOwner> historyOptional = crudRepository.optional(
                "from HistoryOwner where id=:id",
                HistoryOwner.class, Map.of("id", id)
        );
        if (historyOptional.isPresent()) {
            HistoryOwner history = historyOptional.get();
            history.setEndAt(endAt);
            try {
                crudRepository.run(session -> session.update(history));
                rsl = true;
            } catch (Exception e) {
                LOG.error("Failed to update history");
            }
        }
        return rsl;
    }
}
