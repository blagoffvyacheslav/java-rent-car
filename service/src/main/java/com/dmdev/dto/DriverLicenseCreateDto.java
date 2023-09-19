package com.dmdev.dto;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Value
public class DriverLicenseCreateDto {

    @NotNull
    Long userId;

    @NotBlank(message = "Driver license number is mandatory")
    @Size(min = 4, message = "Driver license number should have at least 2 characters")
    String licenseNumber;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate issueDate;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate expiredDate;
}