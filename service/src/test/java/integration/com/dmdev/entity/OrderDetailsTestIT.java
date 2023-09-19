package integration.com.dmdev.entity;

import com.dmdev.entity.OrderDetails;
import integration.com.dmdev.IntegrationBaseTest;
import java.time.LocalDateTime;

import static integration.com.dmdev.entity.OrderTestIT.getExistOrder;

public class OrderDetailsTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_ORDER_DETAILS_ID = 2L;
    public static final Long TEST_ORDER_DETAILS_ID_FOR_DELETE = 1L;

    public static OrderDetails getExistOrderDetails() {
        return OrderDetails.builder()
                .id(TEST_EXISTS_ORDER_DETAILS_ID)
                .order(getExistOrder())
                .startDate(LocalDateTime.of(2023, 7, 10, 0, 0))
                .endDate(LocalDateTime.of(2023, 7, 11, 23, 59))
                .build();
    }

    public static OrderDetails createOrderDetails() {
        return OrderDetails.builder()
                .startDate(LocalDateTime.of(2023, 1, 5, 9, 50))
                .endDate(LocalDateTime.of(2023, 1, 8, 8, 59))
                .build();
    }
}