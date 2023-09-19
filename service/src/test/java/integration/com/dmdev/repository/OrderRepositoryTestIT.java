package integration.com.dmdev.repository;

import com.dmdev.repository.CarRepository;
import com.dmdev.repository.OrderRepository;
import com.dmdev.repository.UserRepository;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.CarBuilder;
import utils.builder.OrderBuilder;
import utils.builder.OrderDetailsBuilder;
import utils.builder.UserBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

class OrderRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Test
    void shouldSaveOrder() {
        var user = userRepository.findById(UserBuilder.TEST_EXISTS_USER_ID).get();
        var car = carRepository.findById(CarBuilder.TEST_EXISTS_CAR_ID).get();
        var orderToSave = OrderBuilder.createOrder();
        orderToSave.setUser(user);
        orderToSave.setCar(car);
        var orderDetails = OrderDetailsBuilder.createOrderDetails();
        orderDetails.setOrder(orderToSave);

        var savedOrder = orderRepository.save(orderToSave);

        assertThat(savedOrder).isNotNull();
    }


    @Test
    void shouldFindByIdOrder() {
        var expectedOrder = Optional.of(OrderBuilder.getExistOrder());

        var actualOrder = orderRepository.findById(OrderBuilder.TEST_EXISTS_ORDER_ID);

        assertThat(actualOrder).isNotNull();
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void shouldUpdateOrder() {
        var startRentalDate = LocalDateTime.of(2022, 10, 11, 13, 0);
        var orderToUpdate = orderRepository.findById(OrderBuilder.TEST_EXISTS_ORDER_ID).get();

        var orderDetails = orderToUpdate.getOrderDetails();
        orderDetails.setStartDate(startRentalDate);
        orderToUpdate.setInsurance(false);
        orderDetails.setOrder(orderToUpdate);

        orderRepository.save(orderToUpdate);

        var updatedOrder = orderRepository.findById(orderToUpdate.getId()).get();

        assertThat(updatedOrder).isEqualTo(orderToUpdate);
        assertThat(updatedOrder.getOrderDetails().getStartDate()).isEqualTo(startRentalDate);
    }

    @Test
    void shouldDeleteOrder() {
        var order = orderRepository.findById(OrderBuilder.TEST_ORDER_ID_FOR_DELETE);

        order.ifPresent(or -> orderRepository.delete(or));

        assertThat(orderRepository.findById(OrderBuilder.TEST_ORDER_ID_FOR_DELETE)).isEmpty();

    }

    @Test
    void shouldFindAllOrders() {

        List<com.dmdev.entity.Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(2);

        List<BigDecimal> amounts = orders.stream().map(com.dmdev.entity.Order::getAmount).collect(toList());
        assertThat(amounts).containsExactlyInAnyOrder(BigDecimal.valueOf(1020.0).setScale(2), BigDecimal.valueOf(10000.0).setScale(2));
    }
}