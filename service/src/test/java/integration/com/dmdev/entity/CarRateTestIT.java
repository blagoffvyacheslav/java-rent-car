package integration.com.dmdev.entity;

import com.dmdev.entity.CarRate;
import com.dmdev.entity.Term;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarRateTestIT extends IntegrationBaseTest {

    public static CarRate getExistCarRate() {
        return CarRate.builder()
                .id(2L)
                .modelId(2L)
                .price(BigDecimal.valueOf(10000.00))
                .term(Term.HOURS)
                .build();
    }

    public static CarRate getUpdatedCarRate() {
        return CarRate.builder()
                .id(2L)
                .modelId(2L)
                .price(BigDecimal.valueOf(5000.00))
                .term(Term.DAYS)
                .build();
    }

    public static CarRate createCarRate() {
        return CarRate.builder()
                .modelId(2L)
                .term(Term.HOURS)
                .build();
    }

    @Test
    public void shouldCreateCarRate() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedCarRateId = (Long) session.save(createCarRate());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedCarRateId);
        }
    }

    @Test
    public void shouldReturnCarRate() {
        try (Session session = sessionFactory.openSession()) {
            CarRate actualCarRate = session.find(CarRate.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualCarRate).isNotNull();
            assertEquals(getExistCarRate().getModelId(), actualCarRate.getModelId());
            assertEquals(getExistCarRate().getPrice(), actualCarRate.getPrice());
            assertEquals(getExistCarRate().getTerm(), actualCarRate.getTerm());
        }
    }

    @Test
    public void shouldUpdateCarRate() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRate carRateToUpdate = getUpdatedCarRate();
            session.update(carRateToUpdate);
            session.getTransaction().commit();

            CarRate updatedCarRate = session.find(CarRate.class, carRateToUpdate.getId());

            assertThat(updatedCarRate).isEqualTo(carRateToUpdate);
        }
    }

    @Test
    public void shouldDeleteCarRate() {
        try (Session session = sessionFactory.openSession()) {
            CarRate carRateToDelete = session.find(CarRate.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(carRateToDelete);
            session.getTransaction().commit();

            assertThat(session.find(CarRate.class, carRateToDelete.getId())).isNull();
        }
    }
}