package integration.com.dmdev.entity;

import com.dmdev.entity.Car;
import com.dmdev.entity.OrderDetails;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderDetailsTestIT extends IntegrationBaseTest {

    public static OrderDetails getExistOrderDetails() {
        return OrderDetails.builder()
                .id(2L)
                .orderId(2L)
                .startDate(LocalDateTime.of(2023, 7, 10, 0, 0))
                .endDate(LocalDateTime.of(2023, 7, 11, 23, 59))
                .build();
    }

    public static OrderDetails getUpdatedOrderDetails() {
        return OrderDetails.builder()
                .id(2L)
                .orderId(2L)
                .startDate(LocalDateTime.of(2023, 8, 4, 00, 00))
                .endDate(LocalDateTime.of(2023, 8, 5, 23, 59))
                .build();
    }

    public static OrderDetails createOrderDetails() {
        return OrderDetails.builder()
                .orderId(3L)
                .startDate(LocalDateTime.of(2023, 1, 5, 9, 50))
                .endDate(LocalDateTime.of(2023, 1, 8, 8, 59))
                .build();
    }

    @Test
    public void shouldCreateOrderDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedCarRentalTimeId = (Long) session.save(createOrderDetails());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedCarRentalTimeId);
        }
    }

    @Test
    public void shouldReturnOrderDetails() {
        try (Session session = sessionFactory.openSession()) {
            OrderDetails actualOrderDetails = session.find(OrderDetails.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualOrderDetails).isNotNull();
            assertEquals(getExistOrderDetails().getOrderId(), actualOrderDetails.getOrderId());
            assertEquals(getExistOrderDetails().getStartDate(), actualOrderDetails.getStartDate());
            assertEquals(getExistOrderDetails().getEndDate(), actualOrderDetails.getEndDate());
        }
    }


    @Test
    public void shouldUpdateOrderDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OrderDetails orderDetailsToUpdate = getUpdatedOrderDetails();
            session.update(orderDetailsToUpdate);
            session.getTransaction().commit();

            OrderDetails updatedCarRentalTime = session.find(OrderDetails.class, orderDetailsToUpdate.getId());

            assertThat(updatedCarRentalTime).isEqualTo(orderDetailsToUpdate);
        }
    }

    @Test
    public void shouldDeleteOrderDetails() {
        try (Session session = sessionFactory.openSession()) {
            Car carToDelete = session.find(Car.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(carToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Car.class, carToDelete.getId())).isNull();
        }
    }
}