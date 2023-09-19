package integration.com.dmdev.service;

import com.dmdev.dto.DriverLicenseUpdateDto;
import com.dmdev.dto.DriverLicenseReadDto;
import com.dmdev.service.DriverLicenseService;
import com.dmdev.service.UserService;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.TestDtoBuilder;
import utils.builder.DriverLicenseBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class DriverLicenseServiceTestIT extends IntegrationBaseTest {

    private final DriverLicenseService driverLicenseService;
    private final UserService userService;

    @Test
    void shouldSaveDriverLicenseCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateDTO();

        var actualUser = userService.create(userCreateRequestDTO);

        assertEquals(userCreateRequestDTO.getDriverLicenseNumber(), actualUser.getDriverLicenseDto().getLicenseNumber());
        assertEquals(userCreateRequestDTO.getDriverLicenseIssueDate(), actualUser.getDriverLicenseDto().getIssueDate());
        assertEquals(userCreateRequestDTO.getDriverLicenseExpiredDate(), actualUser.getDriverLicenseDto().getExpiredDate());
    }

    @Test
    void shouldFindAllDriverLicenses() {
        var driverLicenses = driverLicenseService.getAll(0, 4);

        assertThat(driverLicenses).hasSize(2);
        assertThat(driverLicenses.getTotalElements()).isEqualTo(2L);
        assertThat(driverLicenses.getNumberOfElements()).isEqualTo(2L);

        var addresses = driverLicenses.getContent().stream().map(DriverLicenseReadDto::getLicenseNumber).collect(toList());
        assertThat(addresses).containsExactlyInAnyOrder("12345AB", "12345BD");
    }

    @Test
    void shouldFindAllExpiredDriverLicenses() {
        var driverLicenses = driverLicenseService.getAllExpiredDriverLicenses();

        assertThat(driverLicenses).hasSize(0);
    }


    @Test
    void shouldReturnDriverLicensesByNumber() {
        var userCreateDto = TestDtoBuilder.createUserCreateDTO();
        userService.create(userCreateDto);

        var userDetails = driverLicenseService.getByNumber("ae");

        assertThat(userDetails).isEmpty();
    }

    @Test
    void shouldReturnDriverLicenseById() {
        var userCreateDto = TestDtoBuilder.createUserCreateDTO();
        var expectedUserDetails = userService.create(userCreateDto).getDriverLicenseDto();

        var actualDriverLicense = driverLicenseService.getById(expectedUserDetails.getId());

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(expectedUserDetails, actualDriverLicense);
    }

    @Test
    void shouldUpdateDriverLicenseCorrectly() {
        var userCreateDto = TestDtoBuilder.createUserCreateDTO();
        var driverLicenseUpdateDto = new DriverLicenseUpdateDto(
                "number_test",
                LocalDate.now(),
                LocalDate.now().plusYears(1));
        var savedDriverLicense = userService.create(userCreateDto).getDriverLicenseDto();

        var actualDriverLicense = driverLicenseService.update(savedDriverLicense.getId(), driverLicenseUpdateDto);

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(driverLicenseUpdateDto.getDriverLicenseNumber(), actualDriverLicense.getLicenseNumber());
        assertSame(driverLicenseUpdateDto.getDriverLicenseExpiredDate(), actualDriverLicense.getExpiredDate());
        assertEquals(driverLicenseUpdateDto.getDriverLicenseIssueDate(), actualDriverLicense.getIssueDate());
    }

    @Test
    void shouldDeleteUserDetailByIdCorrectly() {
        assertTrue(driverLicenseService.deleteById(DriverLicenseBuilder.TEST_DRIVER_LICENSE_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(driverLicenseService.deleteById(999999L));
    }
}