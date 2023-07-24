package integration.com.dmdev.repository;

import com.dmdev.dto.OrderFilter;
import com.dmdev.entity.Damage;
import com.dmdev.entity.Car;
import com.dmdev.entity.Order;
import com.dmdev.entity.OrderStatus;
import com.dmdev.repository.OrderRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.OrderTestIT;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTestIT extends IntegrationBaseTest {

    private final OrderRepository orderRepository = OrderRepository.getInstance();

    @Test
    void shouldReturnAllOrdersWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findAllCriteria(session);
            session.getTransaction().commit();

            assertThat(orders).hasSize(2);

            List<LocalDate> ordersData = orders.stream().map(Order::getDate).collect(toList());
            assertThat(ordersData).contains(LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 2));

            List<String> carsNumber = orders.stream()
                    .map(Order::getCar)
                    .map(Car::getSerialNumber)
                    .collect(toList());
            assertThat(carsNumber).contains("0123456", "ABC12345678");
        }
    }

    @Test
    void shouldReturnAllOrdersWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findAllQueryDsl(session);
            session.getTransaction().commit();

            assertThat(orders).hasSize(2);

            List<LocalDate> ordersData = orders.stream().map(Order::getDate).collect(toList());
            assertThat(ordersData).contains(LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 2));

            List<String> carsNumber = orders.stream()
                    .map(Order::getCar)
                    .map(Car::getSerialNumber)
                    .collect(toList());
            assertThat(carsNumber).contains("0123456", "ABC12345678");
        }
    }

    @Test
    void shouldReturnOrderByIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Order> optionalOrder = orderRepository.findByIdCriteria(session, OrderTestIT.TEST_EXISTS_ORDER_ID);
            session.getTransaction().commit();

            assertThat(optionalOrder).isNotNull();
            optionalOrder.ifPresent(order -> assertThat(order).isEqualTo(OrderTestIT.getExistOrder()));
        }
    }

    @Test
    void shouldReturnOrderByIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<Order> optionalOrder = orderRepository.findByIdQueryDsl(session, OrderTestIT.TEST_EXISTS_ORDER_ID);
            session.getTransaction().commit();

            assertThat(optionalOrder).isNotNull();
            optionalOrder.ifPresent(order -> assertThat(order).isEqualTo(OrderTestIT.getExistOrder()));
        }
    }

    @Test
    void shouldReturnOrdersByCarNumberCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findOrdersByCarNumberCriteria(session, "0123456");
            session.getTransaction().commit();

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(OrderTestIT.getExistOrder());
        }
    }

    @Test
    void shouldReturnOrdersOrderStatusCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Order> orders = orderRepository.findOrdersByOrderStatusCriteria(session, OrderStatus.PAYED);
            session.getTransaction().commit();

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(OrderTestIT.getExistOrder());
        }
    }

    @Test
    void shouldReturnOrdersByBrandNameAndModelNameOrderByDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OrderFilter orderFilter = OrderFilter.builder()
                    .modelName("Benz")
                    .build();

            List<Order> orders = orderRepository.findOrdersByBrandNameAndModelNameOrderByDateQueryDsl(session, orderFilter);
            session.getTransaction().commit();

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)).isEqualTo(OrderTestIT.getExistOrder());
        }
    }

    @Test
    void shouldReturnOrdersWhereAccidentsSumMoreThanAvgOrderByDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Order> orders = orderRepository.findOrdersWhereAccidentsSumMoreThanAvgSumOrderByDateQueryDsl(session);
            session.getTransaction().commit();

            assertThat(orders).hasSize(1);
            assertThat(orders.get(0)
                    .getDamages()
                    .stream()
                    .map(Damage::getAmount)
                    .collect(toList()))
                    .contains(BigDecimal.valueOf(100.00).setScale(2));
        }
    }

    @Test
    void shouldReturnTuplesWithAvgSumAndDateOrderByDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<Tuple> orders = orderRepository.findOrderTuplesWithAvgSumAndDateOrderByDateQueryDsl(session);
            session.getTransaction().commit();

            assertThat(orders).hasSize(2);
            List<LocalDate> dates = orders.stream().map(r -> r.get(0, LocalDate.class)).collect(toList());
            assertThat(dates).containsAll(List.of(LocalDate.of(2023, 7, 2), LocalDate.of(2023, 7, 1)));
        }
    }
}