package com.dmdev.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
public class UserDetailsCreateDto {

    @NotNull
    Long userId;

    @NotEmpty
    String name;

    @NotEmpty
    String lastname;

    @NotEmpty
    String address;

    @NotEmpty
    String phone;

    @NotEmpty
    LocalDate birthday;
}