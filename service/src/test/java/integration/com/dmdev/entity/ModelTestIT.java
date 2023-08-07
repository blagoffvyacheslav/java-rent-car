package integration.com.dmdev.entity;

import com.dmdev.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_MODEL_ID = 2L;
    public static final Long TEST_MODEL_ID_FOR_DELETE = 1L;

    public static Model getExistModel() {
        return Model.builder()
                .id(2L)
                .name("Volvo X70")
                .build();
    }


    public static Model createModel() {
        return Model.builder()
                .name("Kia Seed")
                .build();
    }
}