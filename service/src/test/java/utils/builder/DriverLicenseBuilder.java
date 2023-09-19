package utils.builder;

import com.dmdev.entity.DriverLicense;

import java.time.LocalDate;

import static utils.builder.UserDetailsBuilder.getExistUserDetails;

public class DriverLicenseBuilder {

    public static final Long TEST_EXISTS_DRIVER_LICENSE_ID = 2L;
    public static final Long TEST_DRIVER_LICENSE_ID_FOR_DELETE = 1L;

    public static DriverLicense getExistDriverLicense() {
        return DriverLicense.builder()
                .id(TEST_EXISTS_DRIVER_LICENSE_ID)
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