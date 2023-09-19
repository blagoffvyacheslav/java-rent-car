package utils.builder;

import com.dmdev.entity.Car;

import static utils.builder.ModelBuilder.getExistModel;

public class CarBuilder {

    public static final Long TEST_EXISTS_CAR_ID = 2L;
    public static final Long TEST_CAR_ID_FOR_DELETE = 1L;

    public static Car getExistCar() {
        return Car.builder()
                .id(TEST_EXISTS_CAR_ID)
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