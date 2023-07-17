package integration.com.dmdev.entity;

import com.dmdev.entity.DriverLicense;
import com.dmdev.entity.UserDetails;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static integration.com.dmdev.entity.UserDetailsTestIT.getExistUserDetails;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DriverLicenseTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_USER_DETAILS_ID = 2L;
    public static final Long TEST_USER_DETAILS_ID_FOR_DELETE = 1L;

    public static final Long TEST_EXISTS_DRIVER_LICENSE_ID = 2L;
    public static final Long TEST_DRIVER_LICENSE_ID_FOR_DELETE = 1L;

    public static DriverLicense getExistDriverLicense() {
        return DriverLicense.builder()
                .id(2L)
                .userDetails(getExistUserDetails())
                .number("12345BD")
                .issueDate(LocalDate.of(2014, 3, 02))
                .expiredDate(LocalDate.of(2024, 12, 01))
                .build();
    }


    public static DriverLicense createDriverLicense() {
        return DriverLicense.builder()
                .number("12345BQ")
                .issueDate(LocalDate.of(1990, 3, 2))
                .expiredDate(LocalDate.of(1994, 8, 5))
                .build();
    }

    @Test
    public void shouldCreateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserDetails userDetails = session.get(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);
            DriverLicense driverLicenceToSave = createDriverLicense();
            userDetails.setDriverLicense(driverLicenceToSave);

            Long savedDriverLicenseId = (Long) session.save(driverLicenceToSave);
            session.getTransaction().commit();

            assertThat(savedDriverLicenseId).isNotNull();
        }
    }

    @Test
    public void shouldReturnDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            DriverLicense expectedDriverLicense = getExistDriverLicense();

            DriverLicense actualDriverLicense = session.find(DriverLicense.class, TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(actualDriverLicense).isNotNull();
            assertEquals(expectedDriverLicense, actualDriverLicense);
        }
    }

    @Test
    public void shouldUpdateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicense driverLicenseToUpdate = session.find(DriverLicense.class, TEST_EXISTS_DRIVER_LICENSE_ID);
            driverLicenseToUpdate.setNumber("dn36632");

            session.update(driverLicenseToUpdate);
            session.flush();
            session.evict(driverLicenseToUpdate);

            DriverLicense updatedDriverLicense = session.find(DriverLicense.class, driverLicenseToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
        }
    }

    @Test
    public void shouldDeleteDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicense driverLicenseToDelete = session.find(DriverLicense.class, TEST_DRIVER_LICENSE_ID_FOR_DELETE);

            session.delete(driverLicenseToDelete);
            session.getTransaction().commit();

            assertThat(session.find(DriverLicense.class, driverLicenseToDelete.getId())).isNull();
        }
    }
}