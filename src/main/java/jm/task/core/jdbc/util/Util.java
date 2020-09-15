package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Util {
    private static BasicDataSource dataSource;
    private static final Util INSTANCE = new Util();
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    private Util() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
        getDataSource();
    }

    public static Util getInstance() {
        return INSTANCE;
    }

    public BasicDataSource getDataSource() {
        if (dataSource == null) {
            try (InputStream in = Util.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties config = new Properties();
                config.load(in);
                BasicDataSource ds = new BasicDataSource();
                ds.setDriverClassName(config.getProperty("driver-class-name"));
                ds.setUrl(config.getProperty("url"));
                ds.setUsername(config.getProperty("username"));
                ds.setPassword(config.getProperty("password"));
                dataSource = ds;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataSource;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try (InputStream in = Util.class.getClassLoader().getResourceAsStream("db.properties")) {
                Properties config = new Properties();
                config.load(in);
                StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
                Map<String, Object> settings = new HashMap<>();
                settings.put(Environment.DRIVER, config.getProperty("dataSource"));
                settings.put(Environment.URL, config.getProperty("url"));
                settings.put(Environment.USER, config.getProperty("username"));
                settings.put(Environment.PASS, config.getProperty("password"));
                registryBuilder.applySettings(settings);
                registry = registryBuilder.build();
                MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(User.class);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
