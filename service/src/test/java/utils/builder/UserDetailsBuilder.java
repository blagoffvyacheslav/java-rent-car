package utils.builder;

import com.dmdev.entity.UserDetails;

import java.time.LocalDate;

import static utils.builder.UserBuilder.getExistUser;

public class UserDetailsBuilder {

    public static final Long TEST_EXISTS_USER_DETAILS_ID = 2L;
    public static final Long TEST_USER_DETAILS_ID_FOR_DELETE = 1L;

    public static UserDetails getExistUserDetails() {
        return UserDetails.builder()
                .user(getExistUser())
                .id(TEST_EXISTS_USER_DETAILS_ID)
                .name("Vyacheslav")
                .lastname("Blagov")
                .address("17 Lenin st")
                .passportNumber("7212112342")
                .phone("+1 720 123 45 67")
                .birthday(LocalDate.of(1994, 12, 5))
                .registrationDate(LocalDate.of(2023, 7, 3))
                .build();
    }

    public static UserDetails createUserDetails() {
        return UserDetails.builder()
                .name("Semen")
                .lastname("Kobelev")
                .address("Moscow")
                .phone("+74953292540")
                .birthday(LocalDate.of(1995, 10, 4))
                .registrationDate(LocalDate.of(2022, 9, 22))
                .passportNumber("0000000")
                .build();
    }
}