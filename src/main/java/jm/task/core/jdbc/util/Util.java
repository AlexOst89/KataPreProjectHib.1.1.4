package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/katapreproject";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    private SessionFactory sessionFactory;

    public Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();

            Properties props = new Properties();
            props.put(Environment.DRIVER, DRIVER);
            props.put(Environment.URL, URL);
            props.put(Environment.USER, USERNAME);
            props.put(Environment.PASS, PASSWORD);
            props.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
            props.put(Environment.SHOW_SQL, "true");
            props.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            //props.put(Environment.HBM2DDL_AUTO, "update");

            config.setProperties(props);
            config.addAnnotatedClass(jm.task.core.jdbc.model.User.class);

            StandardServiceRegistry sr = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties())
                    .build();

            sessionFactory = config.buildSessionFactory(sr);
        }
        return sessionFactory;
    }
}
