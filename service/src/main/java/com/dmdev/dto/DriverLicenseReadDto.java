package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Value
@Builder
public class DriverLicenseReadDto {

    @NotEmpty
    Long id;

    @NotEmpty
    Long userId;

    String licenseNumber;
    LocalDate issueDate;
    LocalDate expiredDate;
}