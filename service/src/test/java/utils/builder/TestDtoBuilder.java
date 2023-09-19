package utils.builder;

import com.dmdev.dto.*;
import com.dmdev.entity.Role;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class TestDtoBuilder {

    public static CarUpdateDto createCarUpdateDTO(Long modelId) {
        return new CarUpdateDto(modelId, "asdfdsfs", Boolean.TRUE, null);
    }

    public static CarCreateDto createCarCreateDTO(Long modelId) {
        return new CarCreateDto(modelId, "asdfdsfs", Boolean.TRUE, null);
    }

    public static ModelCreateDto createModelCreateDTO() {
        return new ModelCreateDto("Kia Rio 2");
    }

    public static ModelUpdateDto createModelUpdateDTO() {
        return new ModelUpdateDto("Opel Astra");
    }

    public static UserCreateDto createUserCreateDTO() {
        return new UserCreateDto(
                "test@testov.com",
                "test",
                "test",
                "Test",
                "Testov",
                "21 Lebedyanskya st",
                "+79053243424",
                LocalDate.of(2000, 10, 10),
                "0000000",
                LocalDate.of(2020, 10, 10),
                LocalDate.of(2030, 10, 10));
    }

    public static UserCreateDto createUserCreateDTOWithExistsEmail() {
        return new UserCreateDto(
                "admin@gmail.com",
                "test",
                "test",
                "Test",
                "Testov",
                "Testino",
                "+79053243424",
                LocalDate.of(2000, 10, 10),
                "0000000",
                LocalDate.of(2020, 10, 10),
                LocalDate.of(2030, 10, 10));
    }

    public static UserReadDto getUserReadDto() {
        return UserReadDto.builder()
                .email("test@testov.com")
                .username("test")
                .userDetailsDto(UserDetailsReadDto.builder()
                        .name("Test")
                        .lastname("Testov")
                        .address("Testino")
                        .phone("+79053243424")
                        .birthday(LocalDate.of(2000, 10, 10))
                        .build())
                .driverLicenseDto(DriverLicenseReadDto.builder()
                        .licenseNumber("0000000")
                        .issueDate(LocalDate.of(2020, 10, 10))
                        .expiredDate(LocalDate.of(2030, 10, 10))
                        .build())
                .build();
    }

    public static UserUpdateDto createUserUpdateDTO() {
        return new UserUpdateDto(
                "test@testov.com",
                "test",
                Role.CLIENT);
    }
}