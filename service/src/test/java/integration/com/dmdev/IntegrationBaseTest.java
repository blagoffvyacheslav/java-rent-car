package integration.com.dmdev;

import com.dmdev.annotation.IT;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@IT
@TestPropertySource(locations = "/application-test.yaml")
@Sql(scripts = "/inserts.sql")
public abstract class IntegrationBaseTest {

}