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
}