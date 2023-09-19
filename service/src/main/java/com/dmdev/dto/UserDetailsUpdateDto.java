package com.dmdev.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
public class UserDetailsUpdateDto {

    @NotEmpty
    String name;

    @NotEmpty
    String lastname;

    @NotEmpty
    String address;

    @NotEmpty
    String phone;
}