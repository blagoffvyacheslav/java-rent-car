package com.dmdev.dto;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Value
public class UserDetailsCreateDto {

    @NotBlank
    Long userId;

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
}