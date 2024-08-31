package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    Configuration configuration = new Configuration ().addAnnotatedClass (User.class);

    private static final SessionFactory sessionFactory = Util.getSessionFactory ();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("CREATE TABLE IF NOT EXISTS users (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(50) NOT NULL,\n" +
                    "  `lastname` VARCHAR(50) NOT NULL,\n" +
                    "  `age` INT NOT NULL,\n" +
                    "  PRIMARY KEY (`id`));").executeUpdate ();
            session.getTransaction ().commit ();
        } catch (Exception e) {
            sessionFactory.getCurrentSession ().getTransaction ().rollback ();
        }
    }
//
    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("DROP TABLE IF EXISTS users").addEntity (User.class).executeUpdate ();
            session.getTransaction ().commit ();
        } catch (Exception e) {
            sessionFactory.getCurrentSession ().getTransaction ().rollback ();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession ()) {
            session.beginTransaction ();
            session.save (new User (name, lastName, age));
            session.getTransaction ().commit ();
        } catch (Exception e) {
            sessionFactory.getCurrentSession ().getTransaction ().rollback ();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("DELETE FROM users WHERE id=" + id).executeUpdate ();
            session.getTransaction ().commit ();
        } catch (Exception e) {
            sessionFactory.getCurrentSession ().getTransaction ().rollback ();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = sessionFactory.openSession ()) {
            session.beginTransaction ();
            CriteriaBuilder builder = session.getCriteriaBuilder ();
            CriteriaQuery<User> criteria = builder.createQuery (User.class);
            Root<User> root = criteria.from (User.class);
            criteria.select (root);
            users = session.createQuery (criteria).getResultList ();
            session.getTransaction ().commit ();
        } catch (Exception e) {
            sessionFactory.getCurrentSession ().getTransaction ().rollback ();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession ()) {
            session.beginTransaction ();
            session.createSQLQuery ("TRUNCATE TABLE users").executeUpdate ();
            session.getTransaction ().commit ();
        } catch (Exception e) {
            sessionFactory.getCurrentSession ().getTransaction ().rollback ();
        }
    }
}