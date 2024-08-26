package ru.job4j.cars.repository;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Usage {

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        var sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        PostRepository postRepository = new HbmPostRepository(crudRepository);
        Model model1 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Lada")).get();
        Model model2 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Opel")).get();
        Model model3 = crudRepository.optional("from Model where name=:name", Model.class, Map.of("name", "Reno")).get();
        Engine e = new Engine(0, "V8");
        crudRepository.run(session -> session.persist(e));
        Engine engine = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "V8")).get();
        Car c1 = new Car(0, "Granta", engine, model1, new ArrayList<>());
        crudRepository.run(session -> session.persist(c1));
        Car car1 = crudRepository.optional("from Car where name=:name", Car.class, Map.of("name", "Granta")).get();
        Car c2 = new Car(0, "Astra", engine, model2, new ArrayList<>());
        crudRepository.run(session -> session.persist(c2));
        Car car2 = crudRepository.optional("from Car where name=:name", Car.class, Map.of("name", "Astra")).get();
        Car c3 = new Car(0, "Logan", engine, model3, new ArrayList<>());
        crudRepository.run(session -> session.persist(c3));
        Car car3 = crudRepository.optional("from Car where name=:name", Car.class, Map.of("name", "Logan")).get();
        User u = new User(0, "user12", "123");
        crudRepository.run(session -> session.persist(u));
        User user = crudRepository.optional("from User where login=:login", User.class, Map.of("login", "user12")).get();
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
        Post p2 = crudRepository.optional("from Post where description=:description", Post.class, Map.of("description", "desc2")).get();
        Image image1 = new Image(0, "image.png", "/path/image.png", p2);
        crudRepository.run(session -> session.persist(image1));
        Post p4 = crudRepository.optional("from Post where description=:description", Post.class, Map.of("description", "desc4")).get();
        Image image2 = new Image(0, "image2.png", "/path/image2.png", p4);
        crudRepository.run(session -> session.persist(image2));
        List<Post> posts1 = postRepository.findAll();
        List<Post> posts2 = postRepository.findAllWithPhoto();
        List<Post> posts3 = postRepository.findAllByLastDay();
        List<Post> posts4 = postRepository.findAllByModel("Lada");
        System.out.println(1);
        crudRepository.run("delete from Image", Map.of());
        crudRepository.run("delete from Post", Map.of());
        crudRepository.run("delete from Car", Map.of());
        crudRepository.run("delete from Engine", Map.of());
        crudRepository.run("delete from User", Map.of());
    }
}
