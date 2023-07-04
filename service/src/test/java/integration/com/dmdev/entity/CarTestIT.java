package integration.com.dmdev.entity;

import com.dmdev.entity.Car;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTestIT extends IntegrationBaseTest {

    public static Car getExistCar() {
        return Car.builder()
                .id(2L)
                .modelId(2l)
                .serialNumber("ABC12345678")
                .isNew(false)
                .build();
    }

    public static Car getUpdatedCar() {
        return Car.builder()
                .id(2l)
                .modelId(2l)
                .serialNumber("ABD12345678")
                .isNew(true)
                .build();
    }

    public static Car createCar() {
        return Car.builder()
                .modelId(2l)
                .serialNumber("BBD12345678")
                .isNew(false)
                .build();
    }

    @Test
    public void shouldCreateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedCarId = (Long) session.save(createCar());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedCarId);
        }
    }

    @Test
    public void shouldReturnCar() {
        try (Session session = sessionFactory.openSession()) {
            Car actualCar = session.find(Car.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualCar).isNotNull();
            assertEquals(getExistCar().getIsNew(), actualCar.getIsNew());
            assertEquals(getExistCar().getSerialNumber(), actualCar.getSerialNumber());
        }
    }

    @Test
    public void shouldUpdateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Car carToUpdate = getUpdatedCar();
            session.update(carToUpdate);
            session.getTransaction().commit();

            Car updatedCar = session.find(Car.class, carToUpdate.getId());

            assertThat(updatedCar).isEqualTo(carToUpdate);
        }
    }

    @Test
    public void shouldDeleteCar() {
        try (Session session = sessionFactory.openSession()) {
            Car carToDelete = session.find(Car.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(carToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Car.class, carToDelete.getId())).isNull();
        }
    }
}