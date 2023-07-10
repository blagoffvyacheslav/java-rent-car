package integration.com.dmdev.entity;

import com.dmdev.entity.CarRate;
import com.dmdev.entity.Term;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static integration.com.dmdev.entity.ModelTestIT.getExistModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarRateTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_CAR_RATE_ID = 2L;
    public static final Long TEST_CAR_RATE_FOR_DELETE = 1L;

    public static CarRate getExistCarRate() {
        return CarRate.builder()
                .id(2L)
                .price(BigDecimal.valueOf(10000.00))
                .term(Term.HOURS)
                .build();
    }


    public static CarRate createCarRate() {
        return CarRate.builder()
                .model(getExistModel())
                .term(Term.HOURS)
                .build();
    }

    @Test
    public void shouldCreateCarRate() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedCarRateId = (Long) session.save(createCarRate());
            session.getTransaction().commit();

            assertThat(savedCarRateId).isNotNull();
        }
    }

    @Test
    public void shouldReturnCarRate() {
        try (Session session = sessionFactory.openSession()) {
            CarRate expectedPrice = getExistCarRate();

            CarRate actualPrice = session.find(CarRate.class, TEST_EXISTS_CAR_RATE_ID);

            assertThat(actualPrice).isNotNull();
            assertEquals(expectedPrice, actualPrice);
        }
    }

    @Test
    public void shouldUpdateCarRate() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRate carRateToUpdate = session.find(CarRate.class, TEST_EXISTS_CAR_RATE_ID);
            carRateToUpdate.setPrice(BigDecimal.valueOf(67.90));

            session.update(carRateToUpdate);
            session.flush();
            session.evict(carRateToUpdate);

            CarRate updatedCarRate = session.find(CarRate.class, carRateToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedCarRate).isEqualTo(carRateToUpdate);
        }
    }

    @Test
    public void shouldDeleteCarRate() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            CarRate carToDelete = session.find(CarRate.class, TEST_CAR_RATE_FOR_DELETE);

            session.delete(carToDelete);
            session.getTransaction().commit();

            assertThat(session.find(CarRate.class, carToDelete.getId())).isNull();
        }
    }
}