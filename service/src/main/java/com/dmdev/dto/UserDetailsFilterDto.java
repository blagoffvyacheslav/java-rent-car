package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Value
@Builder
public class UserDetailsFilterDto {

    String name;
    String lastname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;

    String phone;
    String address;
}