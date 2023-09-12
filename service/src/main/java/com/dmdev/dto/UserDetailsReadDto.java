package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

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
    LocalDate birthday;
    LocalDate registrationAt;
}