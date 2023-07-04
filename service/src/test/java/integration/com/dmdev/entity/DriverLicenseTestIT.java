package integration.com.dmdev.entity;

import com.dmdev.entity.DriverLicense;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DriverLicenseTestIT extends IntegrationBaseTest {

    public static DriverLicense getExistDriverLicense() {
        return DriverLicense.builder()
                .id(2L)
                .userDetailsId(2L)
                .number("12345BD")
                .issueDate(LocalDate.of(2014, 3, 02))
                .expiredDate(LocalDate.of(2024, 12, 01))
                .build();
    }

    public static DriverLicense getUpdatedDriverLicense() {
        return DriverLicense.builder()
                .id(2L)
                .userDetailsId(2L)
                .number("12345BE")
                .issueDate(LocalDate.of(2015, 3, 02))
                .expiredDate(LocalDate.of(2025, 12, 01))
                .build();
    }

    public static DriverLicense createDriverLicense() {
        return DriverLicense.builder()
                .userDetailsId(3l)
                .number("12345BQ")
                .issueDate(LocalDate.of(1990, 3, 2))
                .expiredDate(LocalDate.of(1994, 8, 5))
                .build();
    }

    @Test
    public void shouldCreateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedDriverLicenseId = (Long) session.save(createDriverLicense());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedDriverLicenseId);
        }
    }

    @Test
    public void shouldReturnDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            DriverLicense actualDriverLicense = session.find(DriverLicense.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualDriverLicense).isNotNull();
            assertEquals(getExistDriverLicense().getUserDetailsId(), actualDriverLicense.getUserDetailsId());
            assertEquals(getExistDriverLicense().getIssueDate(), actualDriverLicense.getIssueDate());
            assertEquals(getExistDriverLicense().getExpiredDate(), actualDriverLicense.getExpiredDate());
            assertEquals(getExistDriverLicense().getNumber(), actualDriverLicense.getNumber());
        }
    }

    @Test
    public void shouldUpdateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicense driverLicenseToUpdate = getUpdatedDriverLicense();
            session.update(driverLicenseToUpdate);
            session.getTransaction().commit();

            DriverLicense updatedDriverLicense = session.find(DriverLicense.class, driverLicenseToUpdate.getId());

            assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
        }
    }

    @Test
    public void shouldDeleteDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            DriverLicense driverLicenseToDelete = session.find(DriverLicense.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(driverLicenseToDelete);
            session.getTransaction().commit();

            assertThat(session.find(DriverLicense.class, driverLicenseToDelete.getId())).isNull();
        }
    }
}