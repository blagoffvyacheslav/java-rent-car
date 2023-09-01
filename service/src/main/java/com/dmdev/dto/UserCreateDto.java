package com.dmdev.dto;

import com.dmdev.entity.Role;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
public class UserCreateDto {

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    @Size(min = 2, message = "Login should have at least 2 characters")
    String login;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String password;

    @NotEmpty
    String name;

    @NotEmpty
    String lastname;

    @NotEmpty
    String address;

    @NotEmpty
    String phone;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    @NotBlank(message = "Driver license number is mandatory")
    @Size(min = 4, message = "Driver license number should have at least 2 characters")
    String driverLicenseNumber;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate driverLicenseIssueDate;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate driverLicenseExpiredDate;

    Role role = Role.ADMIN;

    String passportNumber = "000000";
}