package integration.com.dmdev.entity;

import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTestIT extends IntegrationBaseTest {

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
            Long savedUserId = (Long) session.save(createUser());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedUserId);
        }
    }

    @Test
    public void shouldReturnUser() {
        try (Session session = sessionFactory.openSession()) {
            User actualUser = session.find(User.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualUser).isNotNull();
            assertEquals(getExistUser().getEmail(), actualUser.getEmail());
            assertEquals(getExistUser().getLogin(), actualUser.getLogin());
            assertEquals(getExistUser().getRole(), actualUser.getRole());
        }
    }

    @Test
    public void shouldUpdateUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = getUpdatedUser();
            session.update(userToUpdate);
            session.getTransaction().commit();

            User updatedUser = session.find(User.class, userToUpdate.getId());

            assertThat(updatedUser).isEqualTo(userToUpdate);
        }
    }

    @Test
    public void shouldDeleteUser() {
        try (Session session = sessionFactory.openSession()) {
            User userToDelete = session.find(User.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(userToDelete);
            session.getTransaction().commit();

            assertThat(session.find(User.class, userToDelete.getId())).isNull();
        }
    }
}