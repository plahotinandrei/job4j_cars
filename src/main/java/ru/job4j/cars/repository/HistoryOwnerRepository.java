package ru.job4j.cars.repository;

import ru.job4j.cars.model.HistoryOwner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HistoryOwnerRepository {

    Optional<HistoryOwner> create(int carId, int ownerId, LocalDateTime startAt);

    List<HistoryOwner> findAll();

    List<HistoryOwner> findAllByCarId(int carId);

    List<HistoryOwner> findAllByOwnerId(int ownerId);

    boolean endOwnership(int id, LocalDateTime endAt);
}
