package integration.com.dmdev;

import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import util.HibernateTestUtil;
import java.lang.reflect.Proxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public abstract class IntegrationBaseTest {
    private static final String INSERT_DATA_PATH = "inserts.sql";
    private static final String TRUNCATE_TABLES_PATH = "delete.sql";
    private static final String insert_sql = loadSqlScript(INSERT_DATA_PATH);
    private static final String delete_sql = loadSqlScript(TRUNCATE_TABLES_PATH);
    protected static SessionFactory sessionFactory;

    protected Session createProxySession(SessionFactory sessionFactory) {
        return (Session) Proxy.newProxyInstance(
                SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(insert_sql).executeUpdate();
            session.getTransaction().commit();
        }
    }

//    @BeforeEach
//    void insertData() {
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.createSQLQuery(insert_sql).executeUpdate();
//            session.getTransaction().commit();
//        }
//    }

//    @AfterEach
//    void deleteDataFromAllTables() {
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.createSQLQuery(delete_sql).executeUpdate();
//            session.getTransaction().commit();
//        }
//    }

    @AfterAll
    @SneakyThrows
    static void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(delete_sql).executeUpdate();
            session.getTransaction().commit();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private static String loadSqlScript(String filePath) {
        InputStream inputStream = IntegrationBaseTest.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return bufferedReader.lines().collect(Collectors.joining());
    }
}