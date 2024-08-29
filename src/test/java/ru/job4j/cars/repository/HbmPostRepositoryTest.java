package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.cars.model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class HbmPostRepositoryTest {

    private static StandardServiceRegistry registry;

    private static CrudRepository crudRepository;

    private static PostRepository postRepository;

    @BeforeAll
    public static void init() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        postRepository = new HbmPostRepository(crudRepository);
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @BeforeEach
    public void prepareData() {
        Model model1 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Model model2 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Opel")).get();
        Model model3 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Reno")).get();
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get();
        Car car1 = new Car(0, "Granta", engine, model1, new ArrayList<>());
        crudRepository.run(session -> session.persist(car1));
        Car car2 = new Car(0, "Astra", engine, model2, new ArrayList<>());
        crudRepository.run(session -> session.persist(car2));
        Car car3 = new Car(0, "Logan", engine, model3, new ArrayList<>());
        crudRepository.run(session -> session.persist(car3));
        User user = new User(0, "user", "password");
        crudRepository.run(session -> session.persist(user));
        Post post1 = new Post(0, "desc1", LocalDateTime.now().minusDays(2), user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), car1);
        crudRepository.run(session -> session.persist(post1));
        Post post2 = new Post(0, "desc2", LocalDateTime.now().minusDays(2), user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), car2);
        crudRepository.run(session -> session.persist(post2));
        Post post3 = new Post(0, "desc3", LocalDateTime.now().minusHours(3), user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), car3);
        crudRepository.run(session -> session.persist(post3));
        Post post4 = new Post(0, "desc4", LocalDateTime.now(), user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), car2);
        crudRepository.run(session -> session.persist(post4));
        Post post5 = new Post(0, "desc5", LocalDateTime.now(), user, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), car1);
        crudRepository.run(session -> session.persist(post5));
        Image image1 = new Image(0, "image.png", "/path/image.png", post2);
        crudRepository.run(session -> session.persist(image1));
        Image image2 = new Image(0, "image2.png", "/path/image2.png", post4);
        crudRepository.run(session -> session.persist(image2));
    }

    @AfterEach
    public void wipeTables() {
        crudRepository.run("delete from Image", Map.of());
        crudRepository.run("delete from Post", Map.of());
        crudRepository.run("delete from Car", Map.of());
        crudRepository.run("delete from User", Map.of());
    }

    @Test
    public void whenFindAll() {
        assertThat(postRepository.findAll())
                .map(Post::getDescription)
                .contains("desc1", "desc2", "desc3", "desc4", "desc5");
    }

    @Test
    public void whenFindAllWithPhoto() {
        assertThat(postRepository.findAllWithPhoto())
                .map(Post::getDescription)
                .contains("desc2", "desc4");
    }

    @Test
    public void whenFindAllByLastDay() {
        assertThat(postRepository.findAllByLastDay())
                .map(Post::getDescription)
                .contains("desc3", "desc4", "desc5");
    }

    @Test
    public void whenFindAllByModel() {
        assertThat(postRepository.findAllByModel("Lada"))
                .map(Post::getDescription)
                .contains("desc1", "desc5");
    }
}