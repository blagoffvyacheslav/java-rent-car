package integration.com.dmdev.entity;

import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import integration.com.dmdev.IntegrationBaseTest;


public class UserTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_USER_ID = 2L;
    public static final Long TEST_USER_ID_FOR_DELETE = 1L;

    public static User getExistUser() {
        return User.builder()
                .username("client")
                .id(TEST_EXISTS_USER_ID)
                .email("client@client.com")
                .password("qwerty")
                .role(Role.CLIENT)
                .build();
    }

    public static User createUser() {
        return User.builder()
                .username("admin")
                .email("admin@admin.com")
                .password("qwerty")
                .role(Role.ADMIN)
                .build();
    }
}