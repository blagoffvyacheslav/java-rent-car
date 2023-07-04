package integration.com.dmdev.entity;

import com.dmdev.entity.UserDetails;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDetailsTestIT extends IntegrationBaseTest {

    public static UserDetails getExistUserDetails() {
        return UserDetails.builder()
                .userId(100L)
                .name("Vyacheslav")
                .lastname("Blagov")
                .address("17 Lenin st")
                .passportNumber("7212112342")
                .phone("+1 720 123 45 67")
                .birthday(LocalDate.of(1994, 12, 5))
                .registrationDate(LocalDate.of(2023, 7, 3))
                .build();
    }

    public static UserDetails getUpdatedUserDetails() {
        return UserDetails.builder()
                .id(2L)
                .userId(100L)
                .name("Vyacheslav")
                .lastname("Blagov")
                .address("17 Lenin st, 123")
                .passportNumber("7212112342")
                .phone("+1 720 123 45 67")
                .birthday(LocalDate.of(1994, 12, 5))
                .registrationDate(LocalDate.of(2023, 7, 3))
                .build();
    }

    public static UserDetails createUserDetails() {
        return UserDetails.builder()
                .userId(100L)
                .name("Fake")
                .lastname("Fakov")
                .address("17 Letova st")
                .passportNumber("7212112311")
                .phone("+1 720 123 45 33")
                .birthday(LocalDate.of(1990, 2, 5))
                .registrationDate(LocalDate.of(2023, 7, 3))
                .build();
    }

    @Test
    public void shouldCreateUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedUserDetailsId = (Long) session.save(createUserDetails());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedUserDetailsId);
        }
    }

    @Test
    public void shouldReturnUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            UserDetails actualUserDetails = session.find(UserDetails.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualUserDetails).isNotNull();
            assertEquals(getExistUserDetails().getName(), actualUserDetails.getName());
            assertEquals(getExistUserDetails().getLastname(), actualUserDetails.getLastname());
            assertEquals(getExistUserDetails().getAddress(), actualUserDetails.getAddress());
            assertEquals(getExistUserDetails().getPassportNumber(), actualUserDetails.getPassportNumber());
            assertEquals(getExistUserDetails().getBirthday(), actualUserDetails.getBirthday());
            assertEquals(getExistUserDetails().getPhone(), actualUserDetails.getPhone());
        }
    }

    @Test
    public void shouldUpdateUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserDetails userDetailsToUpdate = getUpdatedUserDetails();
            session.update(userDetailsToUpdate);
            session.getTransaction().commit();

            UserDetails updatedUserDetails = session.find(UserDetails.class, userDetailsToUpdate.getId());

            assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
        }
    }

    @Test
    public void shouldDeleteUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            UserDetails userToDelete = session.find(UserDetails.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(userToDelete);
            session.getTransaction().commit();

            assertThat(session.find(UserDetails.class, userToDelete.getId())).isNull();
        }
    }
}