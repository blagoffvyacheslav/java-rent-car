package utils.builder;

import com.dmdev.entity.CarRate;
import com.dmdev.entity.Term;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static utils.builder.ModelBuilder.getExistModel;

public class CarRateBuilder {

    public static final Long TEST_EXISTS_CAR_RATE_ID = 2L;
    public static final Long TEST_CAR_RATE_FOR_DELETE = 1L;

    public static Set<CarRate> getExistCarRate() {
        Set<CarRate> carRates = new HashSet<>();
        carRates.add(
                CarRate.builder()
                        .id(TEST_EXISTS_CAR_RATE_ID)
                        .price(BigDecimal.valueOf(10000.00))
                        .term(Term.HOURS)
                        .build()
        );
        return carRates;
    }


    public static CarRate createCarRate() {
        return CarRate.builder()
                .model(getExistModel())
                .term(Term.HOURS)
                .build();
    }
}