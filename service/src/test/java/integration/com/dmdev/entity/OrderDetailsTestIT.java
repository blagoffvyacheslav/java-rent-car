package integration.com.dmdev.entity;

import com.dmdev.entity.Order;
import com.dmdev.entity.OrderDetails;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static integration.com.dmdev.entity.OrderTestIT.getExistOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderDetailsTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_ORDER_DETAILS_ID = 2L;
    public static final Long TEST_ORDER_DETAILS_ID_FOR_DELETE = 1L;

    public static OrderDetails getExistOrderDetails() {
        return OrderDetails.builder()
                .id(2L)
                .order(getExistOrder())
                .startDate(LocalDateTime.of(2023, 7, 10, 0, 0))
                .endDate(LocalDateTime.of(2023, 7, 11, 23, 59))
                .build();
    }

    public static OrderDetails createOrderDetails() {
        return OrderDetails.builder()
                .startDate(LocalDateTime.of(2023, 1, 5, 9, 50))
                .endDate(LocalDateTime.of(2023, 1, 8, 8, 59))
                .build();
    }

    @Test
    public void shouldReturnOrderDetails() {
        try (Session session = sessionFactory.openSession()) {
            OrderDetails expectedOrderDetails = getExistOrderDetails();

            OrderDetails actualOrderDetails = session.find(OrderDetails.class, TEST_EXISTS_ORDER_DETAILS_ID);

            assertThat(actualOrderDetails).isNotNull();
            assertEquals(expectedOrderDetails, actualOrderDetails);
        }
    }


    @Test
    public void shouldUpdateOrderDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OrderDetails orderDetailsToUpdate = session.find(OrderDetails.class, TEST_EXISTS_ORDER_DETAILS_ID);

            orderDetailsToUpdate.setEndDate(LocalDateTime.of(2022, 11, 9, 10, 0));
            session.update(orderDetailsToUpdate);
            session.flush();
            session.clear();

            OrderDetails updatedCarRentalTime = session.find(OrderDetails.class, orderDetailsToUpdate.getId());
            Order updatedOrder = session.find(Order.class, orderDetailsToUpdate.getOrder().getId());
            session.getTransaction().commit();

            assertThat(updatedCarRentalTime).isEqualTo(orderDetailsToUpdate);
            assertThat(updatedOrder.getOrderDetails()).isEqualTo(updatedCarRentalTime);
        }
    }

    @Test
    public void shouldDeleteOrderDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OrderDetails orderDetailsToDelete = session.find(OrderDetails.class, TEST_ORDER_DETAILS_ID_FOR_DELETE);
            orderDetailsToDelete.getOrder().setOrderDetails(null);

            session.delete(orderDetailsToDelete);
            session.getTransaction().commit();

            assertThat(session.find(OrderDetails.class, orderDetailsToDelete.getId())).isNull();
        }
    }
}