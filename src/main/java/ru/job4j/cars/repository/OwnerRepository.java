package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;
import java.util.Optional;

public interface OwnerRepository {

    Optional<Owner> create(Owner owner);

    Optional<Owner> findById(int id);

    boolean delete(int id);
}
