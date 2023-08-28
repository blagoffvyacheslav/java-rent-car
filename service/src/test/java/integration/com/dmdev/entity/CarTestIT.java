package integration.com.dmdev.entity;

import com.dmdev.entity.Car;
import com.dmdev.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.entity.ModelTestIT.getExistModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_CAR_ID = 2L;
    public static final Long TEST_CAR_ID_FOR_DELETE = 1L;

    public static Car getExistCar() {
        return Car.builder()
                .id(2L)
                .model(getExistModel())
                .serialNumber("ABC12345678")
                .isNew(false)
                .build();
    }


    public static Car createCar() {
        return Car.builder()
                .serialNumber("BBD12345678")
                .isNew(false)
                .build();
    }
}