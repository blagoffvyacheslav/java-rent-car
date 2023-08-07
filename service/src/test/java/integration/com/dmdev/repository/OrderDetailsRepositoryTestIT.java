package integration.com.dmdev.repository;


import com.dmdev.dto.OrderDetailsFilter;
import com.dmdev.entity.Order;
import com.dmdev.entity.OrderDetails;
import com.dmdev.repository.DriverLicenseRepository;
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
import static org.junit.Assert.assertEquals;

class OrderDetailsRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = createProxySession(sessionFactory);
    private final OrderDetailsRepository orderDetailsRepository = new OrderDetailsRepository(session);

    @Test
    void shouldFindByIdCarRentalTime() {
        session.beginTransaction();
        var expectedCarRentalTime = Optional.of(OrderDetailsTestIT.getExistOrderDetails());

        var actualOrderDetails = orderDetailsRepository.findById(OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);

        assertThat(actualOrderDetails).isNotNull();
        assertEquals(expectedCarRentalTime, actualOrderDetails);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateCarRentalTime() {
        session.beginTransaction();
        var orderDetailsToUpdate = session.find(OrderDetails.class, OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);
        orderDetailsToUpdate.setEndDate(LocalDateTime.of(2022, 11, 9, 10, 0));

        orderDetailsRepository.update(orderDetailsToUpdate);
        session.clear();

        var updatedOrderDetails = session.find(OrderDetails.class, orderDetailsToUpdate.getId());
        var updatedOrder = session.find(Order.class, orderDetailsToUpdate.getOrder().getId());

        assertThat(updatedOrderDetails).isEqualTo(orderDetailsToUpdate);
        assertThat(updatedOrder.getOrderDetails()).isEqualTo(updatedOrderDetails);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteCarRentalTime() {
        session.beginTransaction();
        var orderDetailsToDelete = session.find(OrderDetails.class, OrderDetailsTestIT.TEST_ORDER_DETAILS_ID_FOR_DELETE);
        orderDetailsToDelete.getOrder().setOrderDetails(null);

        orderDetailsRepository.delete(OrderDetailsTestIT.TEST_ORDER_DETAILS_ID_FOR_DELETE);

        assertThat(session.find(OrderDetails.class, OrderDetailsTestIT.TEST_ORDER_DETAILS_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllCarRentalTimes() {
        session.beginTransaction();

        List<OrderDetails> carRentalTimes = orderDetailsRepository.findAll();
        assertThat(carRentalTimes).hasSize(2);

        List<LocalDateTime> startTimes = carRentalTimes.stream().map(OrderDetails::getStartDate).collect(toList());
        assertThat(startTimes).containsExactlyInAnyOrder(
                LocalDateTime.of(2023, 7, 02, 0, 0), LocalDateTime.of(2023, 7, 10, 0, 0));

        session.getTransaction().rollback();
    }
    @Test
    void shouldReturnAllOrderDetailsWithCriteria() {
            session.beginTransaction();
            List<OrderDetails> orderDetails = orderDetailsRepository.findAllCriteria(session);

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
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllOrderDetailsWithQueryDsl() {
            session.beginTransaction();
            List<OrderDetails> orderDetails = orderDetailsRepository.findAllQueryDsl(session);

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
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithCriteria() {
            session.beginTransaction();
            Optional<OrderDetails> optionalTime = orderDetailsRepository.findByIdCriteria(session, OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);

            assertThat(optionalTime).isNotNull();
            optionalTime.ifPresent(orderDetails -> assertThat(orderDetails).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarRentalTimeBYIdWithQueryDsl() {
            session.beginTransaction();
            Optional<OrderDetails> optionalCarRentalTime = orderDetailsRepository.findByIdQueryDsl(session,OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(orderDetails -> assertThat(orderDetails).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));
            session.getTransaction().rollback();

    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithCriteria() {
            session.beginTransaction();
            Optional<OrderDetails> optionalTime = orderDetailsRepository.findOrderDetailsByOrderIdCriteria(session, OrderTestIT.TEST_EXISTS_ORDER_ID);

            assertThat(optionalTime).isNotNull();
            optionalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithQueryDsl() {
            session.beginTransaction();
            Optional<OrderDetails> optionalCarRentalTime = orderDetailsRepository.findOrderDetailsByOrderIdQueryDsl(session, OrderTestIT.TEST_EXISTS_ORDER_ID);

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));
            session.getTransaction().rollback();

    }

    @Test
    void shouldReturnOrderDetailsBetweenStartAndRentalDatesCriteria() {
            session.beginTransaction();
            OrderDetailsFilter carRentalTimeFilter = OrderDetailsFilter.builder()
                    .startDate(LocalDateTime.of(2023, 1, 1, 0, 0,0))
                    .endDate(LocalDateTime.of(2025, 1, 1, 23, 59,0))
                    .build();

            List<OrderDetails> OrderDetails = orderDetailsRepository.findOrderDetailsBetweenStartAndRentalDatesCriteria(session, carRentalTimeFilter);

            assertThat(OrderDetails).hasSize(2);
            assertThat(OrderDetails).contains(OrderDetailsTestIT.getExistOrderDetails());
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnOrderDetailsBetweenStartAndRentalDatesQueryDsl() {
            session.beginTransaction();
            OrderDetailsFilter carRentalTimeFilter = OrderDetailsFilter.builder()
                    .startDate(LocalDateTime.of(2023, 1, 1, 0, 0,0))
                    .endDate(LocalDateTime.of(2025, 1, 1, 23, 59,0))
                    .build();

            List<OrderDetails> orderDetails = orderDetailsRepository.findOrderDetailsBetweenStartAndRentalDatesQueryDsl(session, carRentalTimeFilter);

            assertThat(orderDetails).hasSize(2);
            assertThat(orderDetails).contains(OrderDetailsTestIT.getExistOrderDetails());
            session.getTransaction().rollback();
    }
}