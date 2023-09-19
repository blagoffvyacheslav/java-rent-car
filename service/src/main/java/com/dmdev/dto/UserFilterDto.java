package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Value
@Builder
public class UserFilterDto {

    String username;

    @Email
    String email;

    String name;
    String lastname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate expiredLicenseDriverDate;
}