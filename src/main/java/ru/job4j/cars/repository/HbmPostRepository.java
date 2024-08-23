package ru.job4j.cars.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class HbmPostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Post> findAll() {
        return crudRepository.query("FROM Post p JOIN FETCH p.user", Post.class);
    }

    @Override
    public List<Post> findAllWithPhoto() {
        return crudRepository.query("""
                FROM Post p
                JOIN FETCH p.user
                LEFT JOIN FETCH p.images i
                WHERE SIZE(i) > 0
        """, Post.class);
    }

    @Override
    public List<Post> findAllByLastDay() {
        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        return crudRepository.query("""
                FROM Post p
                JOIN FETCH p.user
                WHERE p.created > :lastDay
        """, Post.class, Map.of("lastDay", lastDay));
    }

    @Override
    public List<Post> findAllByModel(String model) {
        return crudRepository.query("""
                FROM Post p
                JOIN FETCH p.user
                JOIN FETCH p.car c
                JOIN FETCH c.model m
                WHERE m.name=:model
        """, Post.class, Map.of("model", model));
    }
}
