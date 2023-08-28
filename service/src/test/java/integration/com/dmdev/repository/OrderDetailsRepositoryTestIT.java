package integration.com.dmdev.repository;


import com.dmdev.dto.OrderDetailsFilter;
import com.dmdev.entity.Order;
import com.dmdev.entity.OrderDetails;
import com.dmdev.repository.OrderDetailsRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.OrderDetailsTestIT;
import integration.com.dmdev.entity.OrderTestIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

class OrderDetailsRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Test
    void shouldFindByIdCarRentalTime() {
        var expectedCarRentalTime = Optional.of(OrderDetailsTestIT.getExistOrderDetails());

        var actualOrderDetails = orderDetailsRepository.findById(OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);

        assertThat(actualOrderDetails).isNotNull();
        assertEquals(expectedCarRentalTime, actualOrderDetails);
    }

    @Test
    void shouldUpdateCarRentalTime() {
        var orderDetailsToUpdate = orderDetailsRepository.findById(OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID).get();

        orderDetailsToUpdate.setEndDate(LocalDateTime.of(2022, 11, 9, 10, 0));

        orderDetailsRepository.update(orderDetailsToUpdate);

        var updatedOrderDetails = orderDetailsRepository.findById(orderDetailsToUpdate.getId()).get();
        var updatedOrder = orderDetailsRepository.findById(orderDetailsToUpdate.getOrder().getId()).get();

        assertThat(updatedOrderDetails).isEqualTo(orderDetailsToUpdate);
//        assertThat(updatedOrder.getOrderDetails()).isEqualTo(updatedOrderDetails);
    }

    @Test
    void shouldDeleteCarRentalTime() {

        var orderDetailsToDelete = orderDetailsRepository.findById(OrderDetailsTestIT.TEST_ORDER_DETAILS_ID_FOR_DELETE);
        orderDetailsToDelete.ifPresent(crt -> crt.getOrder().setOrderDetails(null));
        orderDetailsToDelete.ifPresent(crt -> orderDetailsRepository.delete(crt));


        assertThat(orderDetailsRepository.findById(OrderDetailsTestIT.TEST_ORDER_DETAILS_ID_FOR_DELETE)).isEmpty();

    }

    @Test
    void shouldFindAllCarRentalTimes() {

        List<OrderDetails> carRentalTimes = orderDetailsRepository.findAll();
        assertThat(carRentalTimes).hasSize(2);

        List<LocalDateTime> startTimes = carRentalTimes.stream().map(OrderDetails::getStartDate).collect(toList());
        assertThat(startTimes).containsExactlyInAnyOrder(
                LocalDateTime.of(2023, 7, 02, 0, 0), LocalDateTime.of(2023, 7, 10, 0, 0));

    }

    @Test
    void shouldReturnAllOrderDetailsWithQueryDsl() {
            List<OrderDetails> orderDetails = orderDetailsRepository.findAllQueryDsl();

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

    @Test
    void shouldReturnCarRentalTimeBYIdWithQueryDsl() {
            Optional<OrderDetails> optionalCarRentalTime = orderDetailsRepository.findByIdQueryDsl(OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(orderDetails -> assertThat(orderDetails).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));

    }

    @Test
    void shouldReturnCarRentalTimeByOrderIdWithQueryDsl() {
            Optional<OrderDetails> optionalCarRentalTime = orderDetailsRepository.findOrderDetailsByOrderIdQueryDsl(OrderTestIT.TEST_EXISTS_ORDER_ID);

            assertThat(optionalCarRentalTime).isNotNull();
            optionalCarRentalTime.ifPresent(carRentalTime -> assertThat(carRentalTime).isEqualTo(OrderDetailsTestIT.getExistOrderDetails()));

    }


    @Test
    void shouldReturnOrderDetailsBetweenStartAndRentalDatesQueryDsl() {
            OrderDetailsFilter carRentalTimeFilter = OrderDetailsFilter.builder()
                    .startDate(LocalDateTime.of(2023, 1, 1, 0, 0,0))
                    .endDate(LocalDateTime.of(2025, 1, 1, 23, 59,0))
                    .build();

            List<OrderDetails> orderDetails = orderDetailsRepository.findOrderDetailsBetweenStartAndRentalDatesQueryDsl(carRentalTimeFilter);

            assertThat(orderDetails).hasSize(2);
            assertThat(orderDetails).contains(OrderDetailsTestIT.getExistOrderDetails());
    }
}