package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Value
@Builder
public class UserDetailsReadDto {

    @NotEmpty
    Long id;

    @NotEmpty
    Long userId;

    String name;
    String lastname;
    String address;
    String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate registrationAt;
}