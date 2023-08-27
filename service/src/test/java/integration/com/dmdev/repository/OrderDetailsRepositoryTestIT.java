package integration.com.dmdev.repository;


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
        var expectedOrderDetails = Optional.of(OrderDetailsTestIT.getExistOrderDetails());

        var actualOrderDetails = orderDetailsRepository.findById(OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID);

        assertThat(actualOrderDetails).isNotNull();
        assertEquals(expectedOrderDetails, actualOrderDetails);
    }

    @Test
    void shouldUpdateCarRentalTime() {
        var orderDetailsToUpdate = orderDetailsRepository.findById(OrderDetailsTestIT.TEST_EXISTS_ORDER_DETAILS_ID).get();

        orderDetailsToUpdate.setEndDate(LocalDateTime.of(2022, 11, 9, 10, 0));

        orderDetailsRepository.save(orderDetailsToUpdate);

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
}