package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class HbmHistoryOwnerRepositoryTest {

    private static StandardServiceRegistry registry;

    private static CrudRepository crudRepository;

    private static HistoryOwnerRepository historyOwnerRepository;

    @BeforeAll
    public static void init() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        historyOwnerRepository = new HbmHistoryOwnerRepository(crudRepository);
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @BeforeEach
    public void prepareData() {
        Model model1 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Model model2 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Opel")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car1 = new Car(0, "Granta", engine, model1, new ArrayList<>());
        crudRepository.run(session -> session.persist(car1));
        Car car2 = new Car(0, "Astra", engine, model2, new ArrayList<>());
        crudRepository.run(session -> session.persist(car2));
        User user = new User(0, "user", "password");
        crudRepository.run(session -> session.persist(user));
        Owner owner = new Owner(0, "Ivan", user, new ArrayList<>());
        crudRepository.run(session -> session.persist(owner));
    }

    @AfterEach
    public void wipeTables() {
        crudRepository.run("delete from HistoryOwner", Map.of());
        crudRepository.run("delete from Car", Map.of());
        crudRepository.run("delete from Owner", Map.of());
        crudRepository.run("delete from User", Map.of());
    }

    @Test
    public void whenCreate() {
        int carId = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Granta"))
                .get().getId();
        int ownerId = crudRepository
                .optional("from Owner where name=:name", Owner.class, Map.of("name", "Ivan"))
                .get().getId();
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        HistoryOwner historyOwner = historyOwnerRepository.create(carId, ownerId, date).get();
        assertThat(historyOwner.getCar().getId()).isEqualTo(carId);
        assertThat(historyOwner.getOwner().getId()).isEqualTo(ownerId);
        assertThat(historyOwner.getStartAt()).isEqualTo(date);
        assertThat(historyOwner.getEndAt()).isNull();
    }

    @Test
    public void whenFindAll() {
        int carId1 = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Granta"))
                .get().getId();
        int carId2 = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Astra"))
                .get().getId();
        int ownerId = crudRepository
                .optional("from Owner where name=:name", Owner.class, Map.of("name", "Ivan"))
                .get().getId();
        LocalDateTime date1 = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime date2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        HistoryOwner historyOwner1 = historyOwnerRepository.create(carId1, ownerId, date1).get();
        HistoryOwner historyOwner2 = historyOwnerRepository.create(carId2, ownerId, date2).get();
        assertThat(historyOwnerRepository.findAll())
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "car.id", "car.name", "owner.id", "owner.name", "startAt", "endAt")
                .isEqualTo(List.of(historyOwner1, historyOwner2));
    }

    @Test
    public void whenByCarId() {
        int carId1 = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Granta"))
                .get().getId();
        int carId2 = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Astra"))
                .get().getId();
        int ownerId = crudRepository
                .optional("from Owner where name=:name", Owner.class, Map.of("name", "Ivan"))
                .get().getId();
        LocalDateTime date1 = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime date2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        HistoryOwner historyOwner1 = historyOwnerRepository.create(carId1, ownerId, date1).get();
        HistoryOwner historyOwner2 = historyOwnerRepository.create(carId2, ownerId, date2).get();
        assertThat(historyOwnerRepository.findAllByCarId(carId1))
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "car.id", "car.name", "owner.id", "owner.name", "startAt", "endAt")
                .isEqualTo(List.of(historyOwner1));
        assertThat(historyOwnerRepository.findAllByCarId(carId2))
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "car.id", "car.name", "owner.id", "owner.name", "startAt", "endAt")
                .isEqualTo(List.of(historyOwner2));
    }

    @Test
    public void whenByOwnerId() {
        int carId1 = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Granta"))
                .get().getId();
        int carId2 = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Astra"))
                .get().getId();
        int ownerId = crudRepository
                .optional("from Owner where name=:name", Owner.class, Map.of("name", "Ivan"))
                .get().getId();
        LocalDateTime date1 = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime date2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        HistoryOwner historyOwner1 = historyOwnerRepository.create(carId1, ownerId, date1).get();
        HistoryOwner historyOwner2 = historyOwnerRepository.create(carId2, ownerId, date2).get();
        assertThat(historyOwnerRepository.findAllByOwnerId(ownerId))
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "car.id", "car.name", "owner.id", "owner.name", "startAt", "endAt")
                .isEqualTo(List.of(historyOwner1, historyOwner2));
    }

    @Test
    public void whenEndOwnership() {
        int carId = crudRepository
                .optional("from Car where name=:name", Car.class, Map.of("name", "Granta"))
                .get().getId();
        int ownerId = crudRepository
                .optional("from Owner where name=:name", Owner.class, Map.of("name", "Ivan"))
                .get().getId();
        LocalDateTime dateStart = LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime dateEnd = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        HistoryOwner historyOwner = historyOwnerRepository.create(carId, ownerId, dateStart).get();
        assertThat(historyOwnerRepository.endOwnership(historyOwner.getId(), dateEnd)).isTrue();
        assertThat(historyOwnerRepository.findAllByCarId(carId).get(0).getEndAt()).isEqualTo(dateEnd);
    }
}