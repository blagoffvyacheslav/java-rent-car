package integration.com.dmdev.repository;

import com.dmdev.dto.DriverLicenseDto;
import com.dmdev.dto.DriverLicenseFilter;
import com.dmdev.entity.DriverLicense;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.repository.DriverLicenseRepository;
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

    private final Session session = createProxySession(sessionFactory);
    private final DriverLicenseRepository driverLicenseRepository = new DriverLicenseRepository(session);

    @Test
    void shouldSaveDriverLicense() {
        session.beginTransaction();
        var userDetails = session.find(UserDetails.class, DriverLicenseTestIT.TEST_EXISTS_USER_DETAILS_ID);
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
        var driverLicenseToUpdate = session.find(DriverLicense.class, DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);
        driverLicenseToUpdate.setNumber("dn36632");

        driverLicenseRepository.update(driverLicenseToUpdate);
        session.evict(driverLicenseToUpdate);

        var updatedDriverLicense = session.find(DriverLicense.class, driverLicenseToUpdate.getId());

        assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteDriverLicense() {
        session.beginTransaction();

        driverLicenseRepository.delete(DriverLicenseTestIT.TEST_DRIVER_LICENSE_ID_FOR_DELETE);

        assertThat(session.find(DriverLicense.class, DriverLicenseTestIT.TEST_DRIVER_LICENSE_ID_FOR_DELETE)).isNull();
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
    void shouldReturnAllDriverLicensesWithCriteria() {
        session.beginTransaction();
        List<DriverLicense> driverLicenses = driverLicenseRepository.findAllCriteria(session);
        session.getTransaction().commit();

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
    void shouldReturnDriverLicenseByIdWithCriteria() {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdCriteria(session, DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(DriverLicenseTestIT.getExistDriverLicense()));
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
    void shouldReturnDriverLicenseByNumberCriteria() {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findDriverLicenseByNumberCriteria(session, "AB12346");

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(DriverLicenseTestIT.getExistDriverLicense()));
            session.getTransaction().rollback();
    }

    @Test
    void shouldNotReturnDriverLicenseByExpiredDateOrLessCriteria() {
            session.beginTransaction();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicenseByExpiredDateOrLessCriteria(session, LocalDate.now().minusDays(1L));

            assertThat(driverLicenses).isEmpty();
            session.getTransaction().rollback();
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateCriteria() {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .issueDate(LocalDate.of(2000, 1, 1))
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateCriteria(session, driverLicenseFilter);

            assertThat(driverLicenses).hasSize(2);
            assertThat(driverLicenses).contains(DriverLicenseTestIT.getExistDriverLicense());
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
    void shouldReturnDriverLicenseDtoByExpiredDateOrderByLastnameCriteria() {
            session.beginTransaction();
            List<DriverLicenseDto> driverLicenseDtos = driverLicenseRepository.findDriverLicensesByExpiredDateOrderBySurnameCriteria(session, LocalDate.of(2030, 1, 1));

            assertThat(driverLicenseDtos).hasSize(2);
            assertThat(driverLicenseDtos.get(0).getNumber()).isEqualTo("12345AB");
            assertThat(driverLicenseDtos.get(1).getNumber()).isEqualTo("12345BD");
            assertThat(driverLicenseDtos.get(0).getSurname()).isEqualTo("Blagov");
            assertThat(driverLicenseDtos.get(1).getSurname()).isEqualTo("Kobelev");
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