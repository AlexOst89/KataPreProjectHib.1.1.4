package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    private final SessionFactory SESSION_FACTORY = new Util().getSessionFactory();

    @Override
    public void createUsersTable() {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT," +
                    "name VARCHAR(45) NOT NULL," +
                    "lastName VARCHAR(45) NOT NULL," +
                    "age INT NOT NULL," +
                    "PRIMARY KEY (id))").executeUpdate();
            transaction.commit();
            LOGGER.info("Таблица успешно создана.");
        } catch (Exception e) {
            LOGGER.severe("Ошибка создания таблицы: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            transaction.commit();
            LOGGER.info("Таблица успешно удалена.");
        } catch (Exception e) {
            LOGGER.severe("Ошибка удаления таблицы: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
            LOGGER.info("Пользователь '" + name + "' успешно сохранён.");
        } catch (Exception e) {
            LOGGER.severe("Ошибка добавления пользователя: " + e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                LOGGER.info("Пользователь с ID=" + id + " успешно удалён.");
            } else {
                LOGGER.warning("Пользователь с данным ID не найден.");
            }
        } catch (Exception e) {
            LOGGER.severe("Ошибка удаления пользователя: " + e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.createQuery("FROM User", User.class).getResultList();
        } catch (Exception e) {
            LOGGER.severe("Ошибка загрузки пользователей: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
            LOGGER.info("Все записи из таблицы users были удалены.");
        } catch (Exception e) {
            LOGGER.severe("Ошибка очистки таблицы: " + e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}