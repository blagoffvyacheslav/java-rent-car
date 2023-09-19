package com.dmdev.dto;

import com.dmdev.entity.Role;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
public class UserCreateDto {

    @NotBlank(message = "Email is mandatory")
    @Email
    String email;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 2, message = "Username should have at least 2 characters")
    String username;

    @NotBlank
    @Size(min = 8, message = "Password should have at least 8 characters")
    @Pattern(regexp = "(?=^.{6,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "Password should have at least 8 characters")
    String password;

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Lastname is required")
    String lastname;

    @NotBlank(message = "Address is required")
    String address;

    @NotBlank(message = "Phone is required")
    String phone;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    @NotBlank(message = "Driver license number is required")
    @Size(min = 4, message = "Driver license number should have at least 2 characters")
    String driverLicenseNumber;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate driverLicenseIssueDate;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate driverLicenseExpiredDate;

    Role role = Role.ADMIN;

    String passportNumber = "000000";
}