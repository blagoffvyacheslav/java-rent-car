package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Value
@Builder
public class DriverLicenseReadDto {

    @NotEmpty
    Long id;

    @NotEmpty
    Long userId;

    String licenseNumber;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate issueDate;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate expiredDate;
}