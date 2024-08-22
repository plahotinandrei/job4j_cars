package ru.job4j.cars.repository;

import ru.job4j.cars.model.Post;
import java.util.List;

public interface PostRepository {

    List<Post> findAll();

    List<Post> findAllWithPhoto();

    List<Post> findAllByLastDay();

    List<Post> findAllByModel(String model);
}
