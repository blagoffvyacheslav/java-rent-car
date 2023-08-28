package integration.com.dmdev;

import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.lang.reflect.Proxy;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.dmdev.configuration.BeansConfiguration;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public abstract class IntegrationBaseTest {
    private static final String INSERT_DATA_PATH = "inserts.sql";
    private static final String TRUNCATE_TABLES_PATH = "delete.sql";
    private static final String insert_sql;

    static {
        try {
            insert_sql = loadSqlScript(INSERT_DATA_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String delete_sql;

    static {
        try {
            delete_sql = loadSqlScript(TRUNCATE_TABLES_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static AnnotationConfigApplicationContext context;

    protected Session createProxySession(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(
                SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }

    @BeforeAll
    static void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeansConfiguration.class);
        Session session = context.getBean(Session.class);
        session.beginTransaction();
        session.createSQLQuery(insert_sql).executeUpdate();
        session.getTransaction().commit();
    }


    @AfterAll
    @SneakyThrows
    static void tearDown() {
        Session session = context.getBean(Session.class);
        session.beginTransaction();
        session.createSQLQuery(delete_sql).executeUpdate();
        session.getTransaction().commit();
    }

    private static String loadSqlScript(String filePath) throws IOException {
        try (InputStream inputStream = IntegrationBaseTest.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
             Stream<String> result = bufferedReader.lines()) {
            return result.collect(joining());
        }
    }
}