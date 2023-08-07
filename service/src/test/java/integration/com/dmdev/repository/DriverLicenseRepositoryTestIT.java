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
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

class DriverLicenseRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = context.getBean(Session.class);
    private final DriverLicenseRepository driverLicenseRepository = context.getBean(DriverLicenseRepository.class);
    private final UserDetailsRepository userDetailsRepository = context.getBean(UserDetailsRepository.class);

    @Test
    void shouldSaveDriverLicense() {
        session.beginTransaction();
        var userDetails = userDetailsRepository.findById(DriverLicenseTestIT.TEST_EXISTS_USER_DETAILS_ID).get();

        var driverLicenceToSave = DriverLicenseTestIT.createDriverLicense();
        userDetails.setDriverLicense(driverLicenceToSave);

        var savedDriverLicense = driverLicenseRepository.save(driverLicenceToSave);

        assertThat(savedDriverLicense).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindByIdDriverLicense() {
        session.beginTransaction();
        var expectedDriverLicense = Optional.of(DriverLicenseTestIT.getExistDriverLicense());

        var actualDriverLicense = driverLicenseRepository.findById(DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);

        assertThat(actualDriverLicense).isNotNull();
        assertEquals(expectedDriverLicense, actualDriverLicense);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateDriverLicense() {
        session.beginTransaction();
        var driverLicenseToUpdate = driverLicenseRepository.findById(DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID).get();

        driverLicenseToUpdate.setNumber("dn36632");

        driverLicenseRepository.update(driverLicenseToUpdate);
        session.evict(driverLicenseToUpdate);

        var updatedDriverLicense = driverLicenseRepository.findById(driverLicenseToUpdate.getId()).get();


        assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteDriverLicense() {
        session.beginTransaction();

        var driverLicense = driverLicenseRepository.findById(DriverLicenseTestIT.TEST_DRIVER_LICENSE_ID_FOR_DELETE);
        driverLicense.ifPresent(dl -> driverLicenseRepository.delete(dl));

        assertThat(driverLicenseRepository.findById(DriverLicenseTestIT.TEST_DRIVER_LICENSE_ID_FOR_DELETE)).isEmpty();

        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllDriverLicences() {
        session.beginTransaction();

        List<DriverLicense> driverLicenses = driverLicenseRepository.findAll();
        assertThat(driverLicenses).hasSize(2);

        List<String> numbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
        assertThat(numbers).containsExactlyInAnyOrder("12345AB", "12345BD");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllDriverLicensesWithQueryDsl() {
            session.beginTransaction();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findAllQueryDsl(session);

            assertThat(driverLicenses).hasSize(2);

            List<String> driverLicenseNumbers = driverLicenses.stream().map(DriverLicense::getNumber).collect(toList());
            assertThat(driverLicenseNumbers).contains("12345AB", "12345BD");

            List<String> userEmails = driverLicenses.stream()
                    .map(DriverLicense::getUserDetails)
                    .map(UserDetails::getUser)
                    .map(User::getEmail)
                    .collect(toList());

            assertThat(userEmails).contains("admin@gmail.com", "client@client.com");
            session.getTransaction().rollback();
        }


    @Test
    void shouldReturnDriverLicenseBIdWithQueryDsl() {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdQueryDsl(session, DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(DriverLicenseTestIT.getExistDriverLicense()));
            session.getTransaction().rollback();
    }


    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateQueryDsl() {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .issueDate(LocalDate.of(2000, 1, 1))
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateQueryDsl(session, driverLicenseFilter);

            assertThat(driverLicenses).hasSize(2);
            assertThat(driverLicenses).contains(DriverLicenseTestIT.getExistDriverLicense());
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicenseTupleByExpiredDateOrderByLastnameQueryDsl() {
            session.beginTransaction();
            List<Tuple> driverLicenses = driverLicenseRepository.findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(session, LocalDate.of(2025, 1, 1));

            assertThat(driverLicenses).hasSize(1);
            List<String> lastname = driverLicenses.stream().map(r -> r.get(1, String.class)).collect(toList());
            assertThat(lastname).contains("Blagov");

            List<String> driverLicenseNumber = driverLicenses.stream().map(r -> r.get(3, String.class)).collect(toList());
            assertThat(driverLicenseNumber).contains("AB12346");
            session.getTransaction().rollback();
    }
}