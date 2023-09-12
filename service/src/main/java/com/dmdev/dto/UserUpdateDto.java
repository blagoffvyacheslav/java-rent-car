package com.dmdev.dto;

import com.dmdev.entity.Role;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class UserUpdateDto {

    @NotEmpty
    @Email
    String email;

    @NotEmpty
    @Size(min = 2, message = "Login should have at least 2 characters")
    String login;

    Role role;
}