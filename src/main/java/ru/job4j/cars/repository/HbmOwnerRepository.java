package ru.job4j.cars.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmOwnerRepository implements OwnerRepository {

    private final CrudRepository crudRepository;

    private final static Logger LOG = LoggerFactory.getLogger(HbmCarRepository.class.getName());

    @Override
    public Optional<Owner> create(Owner owner) {
        Optional<Owner> ownerOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(owner));
            ownerOptional = Optional.of(owner);
        } catch (Exception e) {
            LOG.error("Failed to create owner", e);
        }
        return ownerOptional;
    }

    @Override
    public Optional<Owner> findById(int id) {
        return crudRepository.optional(
                "from Owner o JOIN FETCH o.user where o.id=:id",
                Owner.class, Map.of("id", id)
        );
    }

    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        Optional<Owner> ownerOptional = crudRepository.optional(
                "from Owner c LEFT JOIN FETCH c.history where c.id=:id",
                Owner.class, Map.of("id", id)
        );
        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            try {
                crudRepository.run(session -> session.remove(owner));
                rsl = true;
            } catch (Exception e) {
                LOG.error("Failed to remove car", e);
            }
        }
        return rsl;
    }
}
