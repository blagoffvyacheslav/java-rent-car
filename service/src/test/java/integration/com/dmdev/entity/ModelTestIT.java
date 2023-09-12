package integration.com.dmdev.entity;

import com.dmdev.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;

public class ModelTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_MODEL_ID = 2L;
    public static final Long TEST_MODEL_ID_FOR_DELETE = 1L;

    public static Model getExistModel() {
        return Model.builder()
                .id(TEST_EXISTS_MODEL_ID)
                .name("Volvo X70")
                .build();
    }

    public static Model createModel() {
        return Model.builder()
                .name("Kia Seed")
                .build();
    }
}