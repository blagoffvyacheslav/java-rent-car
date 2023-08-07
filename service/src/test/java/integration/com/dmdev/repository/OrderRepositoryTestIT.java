package integration.com.dmdev.repository;

import com.dmdev.dto.OrderFilter;
import com.dmdev.entity.*;
import com.dmdev.repository.OrderDetailsRepository;
import com.dmdev.repository.OrderRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.CarTestIT;
import integration.com.dmdev.entity.OrderDetailsTestIT;
import integration.com.dmdev.entity.OrderTestIT;
import integration.com.dmdev.entity.UserTestIT;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

class OrderRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = createProxySession(sessionFactory);
    private final OrderRepository orderRepository = new OrderRepository(session);

    @Test
    void shouldSaveOrder() {
        session.beginTransaction();
        var user = session.get(User.class, UserTestIT.TEST_EXISTS_USER_ID);
        var car = session.get(Car.class, CarTestIT.TEST_EXISTS_CAR_ID);
        var orderToSave = OrderTestIT.createOrder();
        orderToSave.setUser(user);
        orderToSave.setCar(car);
        var orderDetails = OrderDetailsTestIT.createOrderDetails();
        orderDetails.setOrder(orderToSave);

        var savedOrder = orderRepository.save(orderToSave);

        assertThat(savedOrder).isNotNull();
        session.getTransaction().rollback();
    }


    @Test
    void shouldFindByIdOrder() {
        session.beginTransaction();
        var expectedOrder = Optional.of(OrderTestIT.getExistOrder());

        var actualOrder = orderRepository.findById(OrderTestIT.TEST_EXISTS_ORDER_ID);

        assertThat(actualOrder).isNotNull();
        assertEquals(expectedOrder, actualOrder);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateOrder() {
        session.beginTransaction();
        var startRentalDate = LocalDateTime.of(2022, 10, 11, 13, 0);
        var orderToUpdate = session.find(Order.class, OrderTestIT.TEST_EXISTS_ORDER_ID);
        var orderDetails = orderToUpdate.getOrderDetails();
        orderDetails.setStartDate(startRentalDate);
        orderToUpdate.setInsurance(false);
        orderDetails.setOrder(orderToUpdate);

        orderRepository.update(orderToUpdate);
        session.clear();

        var updatedOrder = session.find(Order.class, orderToUpdate.getId());

        assertThat(updatedOrder).isEqualTo(orderToUpdate);
        assertThat(updatedOrder.getOrderDetails().getStartDate()).isEqualTo(startRentalDate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteOrder() {
        session.beginTransaction();

        orderRepository.delete(OrderTestIT.TEST_ORDER_ID_FOR_DELETE);

        assertThat(session.find(Order.class, OrderTestIT.TEST_ORDER_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllOrders() {
        session.beginTransaction();

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(2);

        List<BigDecimal> amounts = orders.stream().map(Order::getAmount).collect(toList());
        assertThat(amounts).containsExactlyInAnyOrder(BigDecimal.valueOf(1020.0).setScale(2), BigDecimal.valueOf(10000.0).setScale(2));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllOrdersWithCriteria() {
            session.beginTransaction();
            List<Order> orders = orderRepository.findAllCriteria(session);

            assertThat(orders).hasSize(2);

            List<LocalDate> ordersData = orders.stream().map(Order::getDate).collect(toList());
            assertThat(ordersData).contains(LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 2));

            List<String> carsNumber = orders.stream()
                    .map(Order::getCar)
                    .map(Car::getSerialNumber)
                    .collect(toList());
            assertThat(carsNumber).contains("0123456", "ABC12345678");
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllOrdersWithQueryDsl() {
            session.beginTransaction();
            List<Order> orders = orderRepository.findAllQueryDsl(session);

            assertThat(orders).hasSize(2);

            List<LocalDate> ordersData = orders.stream().map(Order::getDate).collect(toList());
            assertThat(ordersData).contains(LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 2));

            List<String> carsNumber = orders.stream()
                    .map(Order::getCar)
                    .map(Car::getSerialNumber)
                    .collect(toList());
            assertThat(carsNumber).contains("0123456", "ABC12345678");
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrderByIdWithCriteria() {
            session.beginTransaction();
            Optional<Order> optionalOrder = orderRepository.findByIdCriteria(session, OrderTestIT.TEST_EXISTS_ORDER_ID);

            assertThat(optionalOrder).isNotNull();
            optionalOrder.ifPresent(order -> assertThat(order).isEqualTo(OrderTestIT.getExistOrder()));
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrderByIdWithQueryDsl() {
            session.beginTransaction();
            Optional<Order> optionalOrder = orderRepository.findByIdQueryDsl(session, OrderTestIT.TEST_EXISTS_ORDER_ID);

            assertThat(optionalOrder).isNotNull();
            optionalOrder.ifPresent(order -> assertThat(order).isEqualTo(OrderTestIT.getExistOrder()));
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrdersByCarNumberCriteria() {
            session.beginTransaction();
            List<Order> orders = orderRepository.findOrdersByCarNumberCriteria(session, "0123456");

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(OrderTestIT.getExistOrder());
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrdersOrderStatusCriteria() {
            session.beginTransaction();
            List<Order> orders = orderRepository.findOrdersByOrderStatusCriteria(session, OrderStatus.PAYED);

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(OrderTestIT.getExistOrder());
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrdersByBrandNameAndModelNameOrderByDateQueryDsl() {
            session.beginTransaction();
            OrderFilter orderFilter = OrderFilter.builder()
                    .modelName("Benz")
                    .build();

            List<Order> orders = orderRepository.findOrdersByBrandNameAndModelNameOrderByDateQueryDsl(session, orderFilter);
            session.getTransaction().commit();

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(OrderTestIT.getExistOrder());
    }

    @Test
    void shouldReturnOrdersWhereAccidentsSumMoreThanAvgOrderByDateQueryDsl() {
            session.beginTransaction();

            List<Order> orders = orderRepository.findOrdersWhereAccidentsSumMoreThanAvgSumOrderByDateQueryDsl(session);

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)
                    .getDamages()
                    .stream()
                    .map(Damage::getAmount)
                    .collect(toList()))
                    .contains(BigDecimal.valueOf(100.00).setScale(2));
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnTuplesWithAvgSumAndDateOrderByDateQueryDsl() {
            session.beginTransaction();

            List<Tuple> orders = orderRepository.findOrderTuplesWithAvgSumAndDateOrderByDateQueryDsl(session);
            assertThat(orders).hasSize(2);
            List<LocalDate> dates = orders.stream().map(r -> r.get(0, LocalDate.class)).collect(toList());
            assertThat(dates).containsAll(List.of(LocalDate.of(2023, 7, 2), LocalDate.of(2023, 7, 1)));
            session.getTransaction().rollback();
    }
}