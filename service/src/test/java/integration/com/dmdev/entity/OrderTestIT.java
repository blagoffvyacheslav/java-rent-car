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
}