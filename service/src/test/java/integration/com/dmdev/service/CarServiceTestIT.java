package integration.com.dmdev.service;

import com.dmdev.dto.CarReadDto;
import com.dmdev.service.CarService;
import com.dmdev.service.ModelService;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.CarBuilder;
import utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class CarServiceTestIT extends IntegrationBaseTest {

    private final CarService carService;
    private final ModelService modelService;

    @Test
    void shouldSaveCarCorrectly() {
        var modelCreateDto = TestDtoBuilder.createModelCreateDTO();
        var savedModel = modelService.create(modelCreateDto);
        var carCreateDto = TestDtoBuilder.createCarCreateDTO(savedModel.getId());

        var actualCar = carService.create(carCreateDto);

        assertEquals(carCreateDto.getSerialNumber(), actualCar.getSerialNumber());
    }

    @Test
    void shouldFindAllCarsWithDamages() {
        var cars = carService.getAllWithDamages();

        assertThat(cars).hasSize(2);

        var modelNames = cars.stream().map(CarReadDto::getModel).collect(toList());
        assertThat(modelNames).containsExactlyInAnyOrder("Kia Rio", "Volvo X70");
    }

    @Test
    void shouldFindAllCarsWithoutDamages() {
        var cars = carService.getAllWithoutDamages();

        assertThat(cars).hasSize(0);
    }

    @Test
    void shouldFindAllCars() {
        var cars = carService.getAll();

        assertThat(cars).hasSize(2);

        var modelNames = cars.stream().map(CarReadDto::getModel).collect(toList());
        assertThat(modelNames).containsExactlyInAnyOrder("Kia Rio", "Volvo X70");
    }

    @Test
    void shouldReturnCarById() {
        var modelCreateDto = TestDtoBuilder.createModelCreateDTO();
        var savedModel = modelService.create(modelCreateDto);
        var carCreateDto = TestDtoBuilder.createCarCreateDTO(savedModel.getId());
        var expectedCar = carService.create(carCreateDto);

        var actualCar = carService.getById(expectedCar.getId());

        assertThat(actualCar).isNotNull();
        assertEquals(expectedCar, actualCar);
    }

    @Test
    void shouldReturnCarByNumber() {
        var modelCreateDto = TestDtoBuilder.createModelCreateDTO();
        var savedModel = modelService.create(modelCreateDto);
        var carCreateDto = TestDtoBuilder.createCarCreateDTO(savedModel.getId());
        var expectedCar = carService.create(carCreateDto);

        var actualCar = carService.getByCarSerialNumber(expectedCar.getSerialNumber());

        assertThat(actualCar).isNotNull();
        assertEquals(expectedCar, actualCar);
    }

    @Test
    void shouldUpdateCarCorrectly() {
        var modelCreateDto = TestDtoBuilder.createModelCreateDTO();
        var savedModel = modelService.create(modelCreateDto);
        var carCreateDto = TestDtoBuilder.createCarCreateDTO(savedModel.getId());
        var savedCar = carService.create(carCreateDto);

        var carUpdateRequestDto = TestDtoBuilder.createCarUpdateDTO(savedModel.getId());
        var actualCar = carService.update(savedCar.getId(), carUpdateRequestDto);

        assertThat(actualCar).isNotNull();
        assertEquals(carUpdateRequestDto.getSerialNumber(), actualCar.getSerialNumber());
    }

    @Test
    void shouldDeleteCarByIdCorrectly() {
        assertTrue(carService.deleteById(CarBuilder.TEST_CAR_ID_FOR_DELETE));
    }
}