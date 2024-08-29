package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class HbmEngineRepositoryTest {

    private static StandardServiceRegistry registry;

    private static CrudRepository crudRepository;

    private static EngineRepository engineRepository;

    @BeforeAll
    public static void init() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        engineRepository = new HbmEngineRepository(crudRepository);
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    public void whenFindAll() {
        assertThat(engineRepository.findAll())
                .map(Engine::getName)
                .contains("Бензиновый", "Дизельны", "Электрический");
    }

    @Test
    public void whenFindById() {
        int engineId = crudRepository.optional("from Engine where name=:name", Engine.class, Map.of("name", "Бензиновый")).get().getId();
        Engine engine = engineRepository.findById(engineId).get();
        assertThat(engine.getName()).isEqualTo("Бензиновый");
    }

}