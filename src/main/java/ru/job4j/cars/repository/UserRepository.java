package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            System.out.println(user);
            int id = (int) session.save(user);
            user.setId(id);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User SET login = :login, password = :password WHERE id = :id")
                    .setParameter("id", user.getId())
                    .setParameter("login", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :id")
                    .setParameter("id", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> users = List.of();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "FROM User AS u ORDER BY u.id DESC", User.class);
            users = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return users;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Optional<User> userOptional = Optional.empty();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "FROM User AS u WHERE u.id = :id", User.class);
            query.setParameter("id", userId);
            userOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return userOptional;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> users = List.of();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "FROM User AS u WHERE u.login LIKE :key", User.class);
            query.setParameter("key", "%" + key + "%");
            users = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return users;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> userOptional = Optional.empty();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "FROM User AS u WHERE u.login = :login", User.class);
            query.setParameter("login", login);
            userOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return userOptional;
    }
}
