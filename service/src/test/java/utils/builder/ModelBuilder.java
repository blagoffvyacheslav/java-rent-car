package utils.builder;

import com.dmdev.entity.Model;

import static utils.builder.CarRateBuilder.getExistCarRate;
import static utils.builder.UserDetailsBuilder.getExistUserDetails;

public class ModelBuilder {

    public static final Long TEST_EXISTS_MODEL_ID = 2L;
    public static final Long TEST_MODEL_ID_FOR_DELETE = 1L;

    public static Model getExistModel() {
        return Model.builder()
                .id(TEST_EXISTS_MODEL_ID)
                .name("Volvo X70")
                .carRates(getExistCarRate())
                .build();
    }

    public static Model createModel() {
        return Model.builder()
                .name("Kia Seed")
                .build();
    }
}