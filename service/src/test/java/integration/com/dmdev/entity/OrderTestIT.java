package integration.com.dmdev.entity;

import com.dmdev.entity.*;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static integration.com.dmdev.entity.CarTestIT.TEST_EXISTS_CAR_ID;
import static integration.com.dmdev.entity.CarTestIT.getExistCar;
import static integration.com.dmdev.entity.OrderDetailsTestIT.createOrderDetails;
import static integration.com.dmdev.entity.UserTestIT.TEST_EXISTS_USER_ID;
import static integration.com.dmdev.entity.UserTestIT.getExistUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_ORDER_ID = 2L;
    public static final Long TEST_ORDER_ID_FOR_DELETE = 1L;

    public static Order getExistOrder() {
        return Order.builder()
                .id(2l)
                .date(LocalDate.of(2023, 7, 2))
                .user(getExistUser())
                .car(getExistCar())
                .insurance(true)
                .orderStatus(OrderStatus.PAYED)
                .amount(BigDecimal.valueOf(10000.00).setScale(2))
                .build();
    }


    public static Order createOrder() {
        return Order.builder()
                .date(LocalDate.of(2023, 8, 10))
                .insurance(true)
                .orderStatus(OrderStatus.CANCELLED)
                .amount(BigDecimal.valueOf(15))
                .build();
    }

    @Test
    public void shouldCreateOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, TEST_EXISTS_USER_ID);
            Car car = session.get(Car.class, TEST_EXISTS_CAR_ID);
            Order orderToSave = createOrder();
            orderToSave.setUser(user);
            orderToSave.setCar(car);
            OrderDetails orderDetails = createOrderDetails();
            orderDetails.setOrder(orderToSave);

            Long savedOrderId = (Long) session.save(orderToSave);
            session.getTransaction().commit();

            assertThat(savedOrderId).isNotNull();
        }
    }

    @Test
    public void shouldReturnOrder() {
        try (Session session = sessionFactory.openSession()) {
            Order expectedOrder = getExistOrder();

            Order actualOrder = session.find(Order.class, TEST_EXISTS_ORDER_ID);

            assertThat(actualOrder).isNotNull();
            assertEquals(expectedOrder, actualOrder);
        }
    }

    @Test
    public void shouldUpdateOrder() {
        try (Session session = sessionFactory.openSession()) {
            LocalDateTime startDate = LocalDateTime.of(2022, 10, 11, 13, 0);
            session.beginTransaction();
            Order orderToUpdate = session.find(Order.class, TEST_EXISTS_ORDER_ID);
            OrderDetails orderDetails = orderToUpdate.getOrderDetails();
            orderDetails.setStartDate(startDate);
            orderToUpdate.setInsurance(false);
            orderDetails.setOrder(orderToUpdate);

            session.update(orderToUpdate);
            session.flush();
            session.clear();

            Order updatedOrder = session.find(Order.class, orderToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedOrder).isEqualTo(orderToUpdate);
            assertThat(updatedOrder.getOrderDetails().getStartDate()).isEqualTo(startDate);
        }
    }

    @Test
    public void shouldDeleteOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Order orderToDelete = session.find(Order.class, TEST_ORDER_ID_FOR_DELETE);

            session.delete(orderToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Order.class, orderToDelete.getId())).isNull();
            assertThat(session.find(OrderDetails.class, orderToDelete.getOrderDetails().getId())).isNull();
        }
    }
}