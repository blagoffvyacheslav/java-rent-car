package util;

import com.dmdev.utils.HibernateSessionFactoryUtil;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");
    static  {
        postgres.start();
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = HibernateSessionFactoryUtil.buildConfiguration();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
        return configuration.buildSessionFactory();
    }
}
