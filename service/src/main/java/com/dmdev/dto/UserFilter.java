package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserFilter {
    String login;
    String email;
    String password;
    String name;
    String lastname;
    LocalDate birthday;
    LocalDate expiredLicenseDriverDate;
}