package integration.com.dmdev.entity;

import com.dmdev.entity.Car;
import com.dmdev.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.entity.ModelTestIT.getExistModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_CAR_ID = 2L;
    public static final Long TEST_CAR_ID_FOR_DELETE = 1L;

    public static Car getExistCar() {
        return Car.builder()
                .id(2L)
                .model(getExistModel())
                .serialNumber("ABC12345678")
                .isNew(false)
                .build();
    }


    public static Car createCar() {
        return Car.builder()
                .serialNumber("BBD12345678")
                .isNew(false)
                .build();
    }

    @Test
    public void shouldCreateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model model = getExistModel();
            Car car = createCar();
            model.setCar(car);

            Long savedCarId = (Long) session.save(car);
            session.getTransaction().commit();

            assertThat(savedCarId).isNotNull();
        }
    }

    @Test
    public void shouldReturnCar() {
        try (Session session = sessionFactory.openSession()) {
            Car expectedCar = getExistCar();

            Car actualCar = session.find(Car.class, TEST_EXISTS_CAR_ID);

            assertThat(actualCar).isNotNull();
            assertEquals(expectedCar, actualCar);
        }
    }

    @Test
    public void shouldUpdateCar() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Car carToUpdate = session.find(Car.class, TEST_EXISTS_CAR_ID);
            Model existModel = session.find(Model.class, 1L);
            carToUpdate.setModel(existModel);

            session.update(carToUpdate);
            session.flush();
            session.evict(carToUpdate);

            Car updatedCar = session.find(Car.class, carToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedCar).isEqualTo(carToUpdate);
        }
    }

    @Test
    public void shouldDeleteCar() {
        try (Session session = sessionFactory.openSession()) {
            Car carToDelete = session.find(Car.class, TEST_CAR_ID_FOR_DELETE);

            session.delete(carToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Car.class, carToDelete.getId())).isNull();
        }
    }
}