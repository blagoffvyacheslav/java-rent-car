package integration.com.dmdev.entity;

import com.dmdev.entity.Order;
import com.dmdev.entity.OrderStatus;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTestIT extends IntegrationBaseTest {

    public static Order getExistOrder() {
        return Order.builder()
                .id(2l)
                .date(LocalDate.of(2023, 7, 2))
                .userId(2L)
                .carId(2L)
                .insurance(true)
                .orderStatus(OrderStatus.PAYED)
                .amount(BigDecimal.valueOf(10000.00).setScale(2))
                .build();
    }

    public static Order getUpdatedOrder() {
        return Order.builder()
                .id(2l)
                .date(LocalDate.of(2023, 7, 3))
                .userId(2L)
                .carId(2L)
                .insurance(true)
                .orderStatus(OrderStatus.PAYED)
                .amount(BigDecimal.valueOf(500))
                .build();
    }

    public static Order createOrder() {
        return Order.builder()
                .date(LocalDate.of(2023, 8, 10))
                .userId(2L)
                .carId(2L)
                .insurance(true)
                .orderStatus(OrderStatus.CANCELLED)
                .amount(BigDecimal.valueOf(15))
                .build();
    }

    @Test
    public void shouldCreateOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedOrderId = (Long) session.save(createOrder());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedOrderId);
        }
    }

    @Test
    public void shouldReturnOrder() {
        try (Session session = sessionFactory.openSession()) {
            Order actualOrder = session.find(Order.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualOrder).isNotNull();
            assertEquals(getExistOrder().getId(), actualOrder.getId());
            assertEquals(getExistOrder().getCarId(), actualOrder.getCarId());
            assertEquals(getExistOrder().getOrderStatus(), actualOrder.getOrderStatus());
            assertEquals(getExistOrder().getDate(), actualOrder.getDate());
            assertEquals(getExistOrder().getInsurance(), actualOrder.getInsurance());
            assertEquals(getExistOrder().getAmount(), actualOrder.getAmount());
        }
    }

    @Test
    public void shouldUpdateOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Order orderToUpdate = getUpdatedOrder();
            session.update(orderToUpdate);
            session.getTransaction().commit();

            Order updatedOrder = session.find(Order.class, orderToUpdate.getId());

            assertThat(updatedOrder).isEqualTo(orderToUpdate);
        }
    }

    @Test
    public void shouldDeleteOrder() {
        try (Session session = sessionFactory.openSession()) {
            Order orderToDelete = session.find(Order.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(orderToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Order.class, orderToDelete.getId())).isNull();
        }
    }
}