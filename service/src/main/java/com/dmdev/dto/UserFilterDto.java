package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserFilterDto {

    String username;
    String email;
    String name;
    String lastname;
    LocalDate birthday;
    LocalDate expiredLicenseDriverDate;
}