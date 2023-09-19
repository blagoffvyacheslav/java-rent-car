package com.dmdev.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class LoginDto {

    @NotBlank
    @Size(min = 2, message = "Username should have at least 2 characters")
    String username;

    @NotBlank
    @Size(min = 8, message = "Password should have at least 8 characters")
    String password;
}