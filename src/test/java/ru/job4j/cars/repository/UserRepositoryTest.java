package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class UserRepositoryTest {

    private static StandardServiceRegistry registry;

    private static CrudRepository crudRepository;

    private static UserRepository userRepository;

    @BeforeAll
    public static void init() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        userRepository = new UserRepository(crudRepository);
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @AfterEach
    public void wipeTables() {
        crudRepository.run("delete from User", Map.of());
    }

    @Test
    public void whenCreate() {
        User user = new User(0, "user", "password");
        User createdUser = userRepository.create(user);
        assertThat(createdUser).isEqualTo(user);
    }

    @Test
    public void whenUpdate() {
        User user = new User(0, "user", "password");
        userRepository.create(user);
        User updatedUser = new User(user.getId(), "user1", "pass");
        userRepository.update(updatedUser);
        assertThat(userRepository.findById(user.getId()).get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(updatedUser);
    }

    @Test
    public void whenDelete() {
        User user = new User(0, "user", "password");
        userRepository.create(user);
        userRepository.delete(user.getId());
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    public void whenFindAllOrderById() {
        User user1 = new User(0, "user1", "password");
        userRepository.create(user1);
        User user2 = new User(0, "user2", "password");
        userRepository.create(user2);
        assertThat(userRepository.findAllOrderById())
                .usingRecursiveComparison()
                .isEqualTo(List.of(user2, user1));
    }

    @Test
    public void whenFindById() {
        User user = new User(0, "user", "password");
        userRepository.create(user);
        assertThat(userRepository.findById(user.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void whenFindByLikeLogin() {
        User user1 = new User(0, "user", "password");
        userRepository.create(user1);
        User user2 = new User(0, "login", "pass");
        userRepository.create(user2);
        assertThat(userRepository.findByLikeLogin("se"))
                .usingRecursiveComparison()
                .isEqualTo(List.of(user1));
    }

    @Test
    public void whenFindByLogin() {
        User user = new User(0, "user", "password");
        userRepository.create(user);
        assertThat(userRepository.findByLogin("user").get())
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}