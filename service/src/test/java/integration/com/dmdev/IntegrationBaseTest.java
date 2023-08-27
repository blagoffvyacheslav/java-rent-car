package integration.com.dmdev;

import com.dmdev.annotation.IT;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
@ActiveProfiles("test")
@Sql("/inserts.sql")
public abstract class IntegrationBaseTest {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.5")
            .withDatabaseName("cartime")
            .withUsername("postgres")
            .withPassword("pass");

    @BeforeAll
    static void setUp() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}