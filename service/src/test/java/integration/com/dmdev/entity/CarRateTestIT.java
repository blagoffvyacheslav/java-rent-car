package integration.com.dmdev.entity;

import com.dmdev.entity.CarRate;
import com.dmdev.entity.Term;
import integration.com.dmdev.IntegrationBaseTest;
import java.math.BigDecimal;

import static integration.com.dmdev.entity.ModelTestIT.getExistModel;

public class CarRateTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_CAR_RATE_ID = 2L;
    public static final Long TEST_CAR_RATE_FOR_DELETE = 1L;

    public static CarRate getExistCarRate() {
        return CarRate.builder()
                .id(TEST_EXISTS_CAR_RATE_ID)
                .price(BigDecimal.valueOf(10000.00))
                .term(Term.HOURS)
                .build();
    }


    public static CarRate createCarRate() {
        return CarRate.builder()
                .model(getExistModel())
                .term(Term.HOURS)
                .build();
    }
}