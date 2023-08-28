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
}