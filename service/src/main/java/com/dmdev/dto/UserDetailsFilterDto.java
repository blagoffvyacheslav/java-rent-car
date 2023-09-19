package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDetailsFilterDto {

    String name;
    String lastname;
    LocalDate birthday;
    String phone;
    String address;
}