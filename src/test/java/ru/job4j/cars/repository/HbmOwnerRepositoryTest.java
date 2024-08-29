package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class HbmOwnerRepositoryTest {

    private static StandardServiceRegistry registry;

    private static CrudRepository crudRepository;

    private static OwnerRepository ownerRepository;

    @BeforeAll
    public static void init() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        ownerRepository = new HbmOwnerRepository(crudRepository);
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @BeforeEach
    public void prepareData() {
        User user = new User(0, "user", "password");
        crudRepository.run(session -> session.persist(user));
    }

    @AfterEach
    public void wipeTables() {
        crudRepository.run("delete from Owner", Map.of());
        crudRepository.run("delete from User", Map.of());
    }

    @Test
    public void whenCreate() {
        User user = crudRepository.optional("from User where login=:login", User.class, Map.of("login", "user")).get();
        Owner owner = new Owner(0, "Ivan", user, new ArrayList<>());
        assertThat(ownerRepository.create(owner).get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new Owner(0, "Ivan", user, new ArrayList<>()));
    }

    @Test
    public void whenFindById() {
        User user = crudRepository.optional("from User where login=:login", User.class, Map.of("login", "user")).get();
        Owner owner = new Owner(0, "Ivan", user, new ArrayList<>());
        ownerRepository.create(owner);
        assertThat(ownerRepository.findById(owner.getId()).get())
                .usingRecursiveComparison()
                .ignoringFields("history")
                .isEqualTo(owner);
    }

    @Test
    public void whenDelete() {
        User user = crudRepository.optional("from User where login=:login", User.class, Map.of("login", "user")).get();
        Owner owner = new Owner(0, "Ivan", user, new ArrayList<>());
        ownerRepository.create(owner);
        assertThat(ownerRepository.delete(owner.getId())).isTrue();
        assertThat(ownerRepository.findById(owner.getId())).isEmpty();
    }
}