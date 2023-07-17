package integration.com.dmdev.entity;

import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static integration.com.dmdev.entity.UserTestIT.getExistUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDetailsTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_USER_DETAILS_ID = 2L;
    public static final Long TEST_USER_DETAILS_ID_FOR_DELETE = 1L;

    public static UserDetails getExistUserDetails() {
        return UserDetails.builder()
                .user(getExistUser())
                .name("Vyacheslav")
                .lastname("Blagov")
                .address("17 Lenin st")
                .passportNumber("7212112342")
                .phone("+1 720 123 45 67")
                .birthday(LocalDate.of(1994, 12, 5))
                .registrationDate(LocalDate.of(2023, 7, 3))
                .build();
    }


    @Test
    public void shouldReturnUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            UserDetails expectedUserDetails = getExistUserDetails();

            UserDetails actualUserDetails = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);

            assertThat(actualUserDetails).isNotNull();
            assertEquals(expectedUserDetails, actualUserDetails);
        }
    }

    @Test
    public void shouldUpdateUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserDetails userDetailsToUpdate = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);

            session.update(userDetailsToUpdate);
            session.flush();
            session.clear();

            UserDetails updatedUserDetails = session.find(UserDetails.class, userDetailsToUpdate.getId());
            User updatedUser = session.find(User.class, userDetailsToUpdate.getUser().getId());
            session.getTransaction().commit();

            assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
            assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
        }
    }

    @Test
    public void shouldDeleteUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserDetails userDetailsToDelete = session.find(UserDetails.class, TEST_USER_DETAILS_ID_FOR_DELETE);
            userDetailsToDelete.getUser().setUserDetails(null);

            session.delete(userDetailsToDelete);
            session.getTransaction().commit();

            assertThat(session.find(UserDetails.class, userDetailsToDelete.getId())).isNull();
        }
    }
}