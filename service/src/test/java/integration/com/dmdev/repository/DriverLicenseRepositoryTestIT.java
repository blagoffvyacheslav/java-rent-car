package integration.com.dmdev.repository;

import com.dmdev.repository.DriverLicenseRepository;
import com.dmdev.repository.UserDetailsRepository;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.DriverLicenseBuilder;
import utils.builder.UserDetailsBuilder;
import utils.builder.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

class DriverLicenseRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private DriverLicenseRepository driverLicenseRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Test
    void shouldSaveDriverLicense() {
        var userDetails = userDetailsRepository.findById(UserDetailsBuilder.TEST_EXISTS_USER_DETAILS_ID).get();

        var driverLicenceToSave = DriverLicenseBuilder.createDriverLicense();
        userDetails.setDriverLicense(driverLicenceToSave);

        var savedDriverLicense = driverLicenseRepository.save(driverLicenceToSave);

        assertThat(savedDriverLicense).isNotNull();
    }

    @Test
    void shouldFindByIdDriverLicense() {
        var expectedDriverLicense = Optional.of(DriverLicenseBuilder.getExistDriverLicense());

        var actualDriverLicense = driverLicenseRepository.findById(DriverLicenseBuilder.TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(expectedDriverLicense, actualDriverLicense);
    }

    @Test
    void shouldUpdateDriverLicense() {
        var driverLicenseToUpdate = driverLicenseRepository.findById(DriverLicenseBuilder.TEST_EXISTS_DRIVER_LICENSE_ID).get();

        driverLicenseToUpdate.setNumber("dn36632");

        driverLicenseRepository.save(driverLicenseToUpdate);

        var updatedDriverLicense = driverLicenseRepository.findById(driverLicenseToUpdate.getId()).get();


        assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
    }

    @Test
    void shouldDeleteDriverLicense() {

        var driverLicense = driverLicenseRepository.findById(DriverLicenseBuilder.TEST_DRIVER_LICENSE_ID_FOR_DELETE);
        driverLicense.ifPresent(dl -> driverLicenseRepository.delete(dl));

        assertThat(driverLicenseRepository.findById(DriverLicenseBuilder.TEST_DRIVER_LICENSE_ID_FOR_DELETE)).isEmpty();

    }

    @Test
    void shouldFindAllDriverLicences() {

        List<com.dmdev.entity.DriverLicense> driverLicenses = driverLicenseRepository.findAll();
        assertThat(driverLicenses).hasSize(2);

        List<String> numbers = driverLicenses.stream().map(com.dmdev.entity.DriverLicense::getNumber).collect(toList());
        assertThat(numbers).containsExactlyInAnyOrder("12345AB", "12345BD");
    }

    @Test
    void shouldReturnDriverLicenseByNumber() {
        var driverLicenses = driverLicenseRepository.findByNumberContainingIgnoreCase("12345BD");

        assertThat(driverLicenses).hasSize(1);
        assertThat(driverLicenses.get(0).getId()).isEqualTo(DriverLicenseBuilder.getExistDriverLicense().getId());
        assertThat(driverLicenses.get(0)).isEqualTo(DriverLicenseBuilder.getExistDriverLicense());
    }

    @Test
    void shouldNotReturnDriverLicenseByExpiredDateOrLess() {
        List<com.dmdev.entity.DriverLicense> driverLicenses = driverLicenseRepository.findByExpiredDateLessThanEqual(LocalDate.now().minusDays(1L));

        assertThat(driverLicenses).isEmpty();
    }

    @Test
    void shouldReturnDriverLicenseByUserId() {
        List<com.dmdev.entity.DriverLicense> driverLicenses = driverLicenseRepository.findByUserId(UserBuilder.TEST_EXISTS_USER_ID);
        assertThat(driverLicenses).hasSize(1);
        assertEquals(driverLicenses.get(0), DriverLicenseBuilder.getExistDriverLicense());
    }
}