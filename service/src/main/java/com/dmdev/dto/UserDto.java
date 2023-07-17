package com.dmdev.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class UserDto {
    String email;
    String name;
    String lastname;
    LocalDate birthday;
    String address;
}