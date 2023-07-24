package integration.com.dmdev.repository;


import com.dmdev.dto.OrderDetailsFilter;
import com.dmdev.entity.OrderDetails;
import com.dmdev.repository.OrderDetailsRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.OrderDetailsTestIT;
import integration.com.dmdev.entity.OrderTestIT;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class OrderDetailsRepositoryTestIT extends IntegrationBaseTest {

    private final OrderDetailsRepository orderDetailsRepository = OrderDetailsRepository.getInstance();

    @Test
    void shouldReturnAllOrderDetailsWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OrderDetails> orderDetails = orderDetailsRepository.findAllCriteria(session);
            session.getTransaction().commit();

            assertThat(orderDetails).hasSize(2);

            List<String> startTimes = orderDetails.stream()
                    .map(OrderDetails::getStartDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            List<String> endTimes = orderDetails.stream()
                    .map(OrderDetails::getEndDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            assertThat(startTimes).containsExactlyInAnyOrder("2023-07-02T00:00", "2023-07-10T00:00");
            assertThat(endTimes).containsExactlyInAnyOrder("2023-07-03T00:00", "2023-07-11T23:59");
        }
    }

    @Test
    void shouldReturnAllOrderDetailsWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OrderDetails> orderDetails = orderDetailsRepository.findAllQueryDsl(session);
            session.getTransaction().commit();

            assertThat(orderDetails).hasSize(2);

            List<String> startTimes = orderDetails.stream()
                    .map(OrderDetails::getStartDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            List<String> endTimes = orderDetails.stream()
                    .map(OrderDetails::getEndDate)
                    .map(LocalDateTime::toString)
                    .collect(toList());

            assertThat(startTimes).containsExactlyInAnyOrder("2023-07-02T00:00", "2023-07-10T00:00");
            assertThat(endTimes).containsExactlyInAnyOrder("2023-07-03T00:00", "2023-07-11T23:59");
        }
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<OrderDetails> optionalTime = orderDetailsRepository.findByIdCriteria(session, OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);
            session.getTransaction().commit();

            assertThat(optionalTime).isNotNull();
            optionalTime.ifPresent(orderDetails -> assertThat(orderDetails).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));

        }
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<OrderDetails> optionalCarRentalTime = orderDetailsRepository.findByIdQueryDsl(session,OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);
            session.getTransaction().commit();

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(orderDetails -> assertThat(orderDetails).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));

        }
    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<OrderDetails> optionalTime = orderDetailsRepository.findOrderDetailsByOrderIdCriteria(session, OrderTestIT.TEST_EXISTS_ORDER_ID);
            session.getTransaction().commit();

            assertThat(optionalTime).isNotNull();
            optionalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));

        }
    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<OrderDetails> optionalCarRentalTime = orderDetailsRepository.findOrderDetailsByOrderIdQueryDsl(session, OrderTestIT.TEST_EXISTS_ORDER_ID);
            session.getTransaction().commit();

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));

        }
    }

    @Test
    void shouldReturnOrderDetailsBetweenStartAndRentalDatesCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OrderDetailsFilter carRentalTimeFilter = OrderDetailsFilter.builder()
                    .startDate(LocalDateTime.of(2023, 1, 1, 0, 0,0))
                    .endDate(LocalDateTime.of(2025, 1, 1, 23, 59,0))
                    .build();

            List<OrderDetails> OrderDetails = orderDetailsRepository.findOrderDetailsBetweenStartAndRentalDatesCriteria(session, carRentalTimeFilter);
            session.getTransaction().commit();

            assertThat(OrderDetails).hasSize(2);
            assertThat(OrderDetails).contains(OrderDetailsTestIT.getExistOrderDetails());

        }
    }

    @Test
    void shouldReturnOrderDetailsBetweenStartAndRentalDatesQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OrderDetailsFilter carRentalTimeFilter = OrderDetailsFilter.builder()
                    .startDate(LocalDateTime.of(2023, 1, 1, 0, 0,0))
                    .endDate(LocalDateTime.of(2025, 1, 1, 23, 59,0))
                    .build();

            List<OrderDetails> orderDetails = orderDetailsRepository.findOrderDetailsBetweenStartAndRentalDatesQueryDsl(session, carRentalTimeFilter);
            session.getTransaction().commit();

            assertThat(orderDetails).hasSize(2);
            assertThat(orderDetails).contains(OrderDetailsTestIT.getExistOrderDetails());

        }
    }
}