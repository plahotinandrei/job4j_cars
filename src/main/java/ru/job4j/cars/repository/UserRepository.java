package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        crudRepository.run(session -> session.persist(user));
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        crudRepository.run(
                "UPDATE User SET login = :login, password = :password WHERE id = :id",
                Map.of("id", user.getId(), "login", user.getLogin(), "password", user.getPassword())
        );
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        crudRepository.run("DELETE User WHERE id = :id", Map.of("id", userId));
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        return crudRepository.query("FROM User AS u ORDER BY u.id DESC", User.class);
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        return crudRepository.optional("FROM User AS u WHERE u.id = :id", User.class, Map.of("id", userId));
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "FROM User AS u WHERE u.login LIKE :key",
                User.class, Map.of("key", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "FROM User AS u WHERE u.login = :login",
                User.class, Map.of("login", login)
        );
    }
}
