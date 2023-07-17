package integration.com.dmdev.entity;

import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_USER_ID = 2L;
    public static final Long TEST_USER_ID_FOR_DELETE = 1L;

    public static User getExistUser() {
        return User.builder()
                .login("client")
                .email("client@client.com")
                .password("qwerty")
                .role(Role.CLIENT)
                .build();
    }

    public static User getUpdatedUser() {
        return User.builder()
                .id(2l)
                .login("admin2")
                .email("admin2@gmail.com")
                .password("qwerty2")
                .role(Role.CLIENT)
                .build();
    }

    public static User createUser() {
        return User.builder()
                .login("admin")
                .email("admin@admin.com")
                .password("qwerty")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    public void shouldCreateUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToSave = createUser();

            Long savedUserId = (Long) session.save(userToSave);
            session.getTransaction().commit();

            assertThat(savedUserId).isNotNull();
        }
    }

    @Test
    public void shouldReturnUser() {
        try (Session session = sessionFactory.openSession()) {
            User expectedUser = getExistUser();

            User actualUser = session.find(User.class, TEST_EXISTS_USER_ID);

            assertThat(actualUser).isNotNull();
            assertEquals(expectedUser, actualUser);
        }
    }

    @Test
    public void shouldUpdateUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = session.find(User.class, TEST_EXISTS_USER_ID);
            UserDetails userDetails = userToUpdate.getUserDetails();
            userToUpdate.setPassword("1111");
            userDetails.setUser(userToUpdate);

            session.update(userToUpdate);
            session.flush();
            session.clear();

            User updatedUser = session.find(User.class, userToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedUser).isEqualTo(userToUpdate);
        }
    }

    @Test
    public void shouldDeleteUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToDelete = session.find(User.class, TEST_USER_ID_FOR_DELETE);

            session.delete(userToDelete);
            session.getTransaction().commit();

            assertThat(session.find(User.class, userToDelete.getId())).isNull();
        }
    }
}