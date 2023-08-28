package integration.com.dmdev.repository;

import com.dmdev.dto.DriverLicenseFilter;
import com.dmdev.entity.DriverLicense;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.repository.DriverLicenseRepository;
import com.dmdev.repository.UserDetailsRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.DriverLicenseTestIT;
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
        var userDetails = userDetailsRepository.findById(DriverLicenseTestIT.TEST_EXISTS_USER_DETAILS_ID).get();

        var driverLicenceToSave = DriverLicenseTestIT.createDriverLicense();
        userDetails.setDriverLicense(driverLicenceToSave);

        var savedDriverLicense = driverLicenseRepository.save(driverLicenceToSave);

        assertThat(savedDriverLicense).isNotNull();
    }

    @Test
    void shouldFindByIdDriverLicense() {
        var expectedDriverLicense = Optional.of(DriverLicenseTestIT.getExistDriverLicense());

        var actualDriverLicense = driverLicenseRepository.findById(DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(expectedDriverLicense, actualDriverLicense);
    }

    @Test
    void shouldUpdateDriverLicense() {
        var driverLicenseToUpdate = driverLicenseRepository.findById(DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID).get();

        driverLicenseToUpdate.setNumber("dn36632");

        driverLicenseRepository.update(driverLicenseToUpdate);

        var updatedDriverLicense = driverLicenseRepository.findById(driverLicenseToUpdate.getId()).get();


        assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
    }

    @Test
    void shouldDeleteDriverLicense() {

        var driverLicense = driverLicenseRepository.findById(DriverLicenseTestIT.TEST_DRIVER_LICENSE_ID_FOR_DELETE);
        driverLicense.ifPresent(dl -> driverLicenseRepository.delete(dl));

        assertThat(driverLicenseRepository.findById(DriverLicenseTestIT.TEST_DRIVER_LICENSE_ID_FOR_DELETE)).isEmpty();

    }

    @Test
    void shouldFindAllDriverLicences() {

        List<DriverLicense> driverLicenses = driverLicenseRepository.findAll();
        assertThat(driverLicenses).hasSize(2);

        List<String> numbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
        assertThat(numbers).containsExactlyInAnyOrder("12345AB", "12345BD");
    }

    @Test
    void shouldReturnAllDriverLicensesWithQueryDsl() {
            List<DriverLicense> driverLicenses = driverLicenseRepository.findAllQueryDsl();

            assertThat(driverLicenses).hasSize(2);

            List<String> driverLicenseNumbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
            assertThat(driverLicenseNumbers).contains("12345AB", "12345BD");

            List<String> userEmails = driverLicenses.stream()
                    .map(DriverLicense::getUserDetails)
                    .map(UserDetails::getUser)
                    .map(User::getEmail)
                    .collect(toList());

            assertThat(userEmails).contains("admin@gmail.com", "client@client.com");
        }


    @Test
    void shouldReturnDriverLicenseBIdWithQueryDsl() {
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdQueryDsl(DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(DriverLicenseTestIT.getExistDriverLicense()));
    }


    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateQueryDsl() {
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .issueDate(LocalDate.of(2000, 1, 1))
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateQueryDsl(driverLicenseFilter);

            assertThat(driverLicenses).hasSize(2);
            assertThat(driverLicenses).contains(DriverLicenseTestIT.getExistDriverLicense());
    }

    @Test
    void shouldReturnDriverLicenseTupleByExpiredDateOrderByLastnameQueryDsl() {
            List<Tuple> driverLicenses = driverLicenseRepository.findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(LocalDate.of(2025, 1, 1));

            assertThat(driverLicenses).hasSize(1);
            List<String> lastname = driverLicenses.stream().map(r -> r.get(1, String.class)).collect(toList());
            assertThat(lastname).contains("Blagov");

            List<String> driverLicenseNumber = driverLicenses.stream().map(r -> r.get(3, String.class)).collect(toList());
            assertThat(driverLicenseNumber).contains("AB12346");
    }
}