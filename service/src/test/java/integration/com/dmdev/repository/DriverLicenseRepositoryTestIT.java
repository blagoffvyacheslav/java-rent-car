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

class DriverLicenseRepositoryTestIT extends IntegrationBaseTest {

    private final DriverLicenseRepository driverLicenseRepository = DriverLicenseRepository.getInstance();

    @Test
    void shouldReturnAllDriverLicensesWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
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
        }
    }

    @Test
    void shouldReturnAllDriverLicensesWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findAllQueryDsl(session);
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
        }
    }

    @Test
    void shouldReturnDriverLicenseByIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdCriteria(session, DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);
            session.getTransaction().commit();

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(DriverLicenseTestIT.getExistDriverLicense()));
        }
    }

    @Test
    void shouldReturnDriverLicenseBIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findByIdQueryDsl(session, DriverLicenseTestIT.TEST_EXISTS_DRIVER_LICENSE_ID);
            session.getTransaction().commit();

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(DriverLicenseTestIT.getExistDriverLicense()));
        }
    }

    @Test
    void shouldReturnDriverLicenseByNumberCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<DriverLicense> optionalDriverLicense = driverLicenseRepository.findDriverLicenseByNumberCriteria(session, "AB12346");
            session.getTransaction().commit();

            assertThat(optionalDriverLicense).isNotNull();
            optionalDriverLicense.ifPresent(driverLicense -> assertThat(driverLicense).isEqualTo(DriverLicenseTestIT.getExistDriverLicense()));
        }
    }

    @Test
    void shouldNotReturnDriverLicenseByExpiredDateOrLessCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicenseByExpiredDateOrLessCriteria(session, LocalDate.now().minusDays(1L));
            session.getTransaction().commit();

            assertThat(driverLicenses).isEmpty();
        }
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .issueDate(LocalDate.of(2000, 1, 1))
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateCriteria(session, driverLicenseFilter);
            session.getTransaction().commit();

            assertThat(driverLicenses).hasSize(2);
            assertThat(driverLicenses).contains(DriverLicenseTestIT.getExistDriverLicense());
        }
    }

    @Test
    void shouldReturnDriverLicensesByIssueAndExpiredDateQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicenseFilter driverLicenseFilter = DriverLicenseFilter.builder()
                    .issueDate(LocalDate.of(2000, 1, 1))
                    .expiredDate(LocalDate.of(2030, 1, 1))
                    .build();
            List<DriverLicense> driverLicenses = driverLicenseRepository.findDriverLicensesByIssueAndExpiredDateQueryDsl(session, driverLicenseFilter);
            session.getTransaction().commit();

            assertThat(driverLicenses).hasSize(2);
            assertThat(driverLicenses).contains(DriverLicenseTestIT.getExistDriverLicense());
        }
    }

    @Test
    void shouldReturnDriverLicenseDtoByExpiredDateOrderByLastnameCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<DriverLicenseDto> driverLicenseDtos = driverLicenseRepository.findDriverLicensesByExpiredDateOrderBySurnameCriteria(session, LocalDate.of(2030, 1, 1));
            session.getTransaction().commit();

            assertThat(driverLicenseDtos).hasSize(2);
            assertThat(driverLicenseDtos.get(0).getNumber()).isEqualTo("12345AB");
            assertThat(driverLicenseDtos.get(1).getNumber()).isEqualTo("12345BD");
            assertThat(driverLicenseDtos.get(0).getSurname()).isEqualTo("Blagov");
            assertThat(driverLicenseDtos.get(1).getSurname()).isEqualTo("Kobelev");
        }
    }

    @Test
    void shouldReturnDriverLicenseTupleByExpiredDateOrderByLastnameQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Tuple> driverLicenses = driverLicenseRepository.findDriverLicensesTupleByExpiredDateOrderBySurnameQueryDsl(session, LocalDate.of(2025, 1, 1));
            session.getTransaction().commit();

            assertThat(driverLicenses).hasSize(1);
            List<String> lastname = driverLicenses.stream().map(r -> r.get(1, String.class)).collect(toList());
            assertThat(lastname).contains("Blagov");

            List<String> driverLicenseNumber = driverLicenses.stream().map(r -> r.get(3, String.class)).collect(toList());
            assertThat(driverLicenseNumber).contains("AB12346");
        }
    }
}