package utils.builder;

import com.dmdev.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static utils.builder.CarBuilder.getExistCar;
import static utils.builder.UserBuilder.getExistUser;

public class OrderBuilder {

    public static final Long TEST_EXISTS_ORDER_ID = 2L;
    public static final Long TEST_ORDER_ID_FOR_DELETE = 1L;

    public static Order getExistOrder() {
        return Order.builder()
                .id(TEST_EXISTS_ORDER_ID)
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