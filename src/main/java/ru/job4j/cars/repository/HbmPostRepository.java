package ru.job4j.cars.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Model;
import ru.job4j.cars.model.Post;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HbmPostRepository implements PostRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Post> findAll() {
        return crudRepository.query("from Post p JOIN FETCH p.user", Post.class);
    }

    @Override
    public List<Post> findAllWithPhoto() {
        return crudRepository.tx(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Post> cq = cb.createQuery(Post.class);
            Root<Post> post = cq.from(Post.class);
            post.fetch("user");
            post.join("images", JoinType.LEFT);
            Predicate hasImages = cb.isNotEmpty(post.get("images"));
            cq.select(post).where(hasImages);
            TypedQuery<Post> query = session.createQuery(cq);
            return query.getResultList();
        });
    }

    @Override
    public List<Post> findAllByLastDay() {
        return crudRepository.tx(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Post> cq = cb.createQuery(Post.class);
            Root<Post> post = cq.from(Post.class);
            post.fetch("user");
            LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
            Predicate lastDayPredicate = cb.greaterThanOrEqualTo(post.get("created"), lastDay);
            cq.select(post).where(lastDayPredicate);
            TypedQuery<Post> query = session.createQuery(cq);
            return query.getResultList();
        });
    }

    @Override
    public List<Post> findAllByModel(String model) {
        return crudRepository.tx(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Post> cq = cb.createQuery(Post.class);
            Root<Post> post = cq.from(Post.class);
            post.fetch("user");
            Join<Post, Car> carJoin = post.join("car");
            Join<Car, Model> modelJoin = carJoin.join("model");
            Predicate modelPredicate = cb.equal(modelJoin.get("name"), model);
            cq.select(post).where(modelPredicate);
            TypedQuery<Post> query = session.createQuery(cq);
            return query.getResultList();
        });
    }
}
