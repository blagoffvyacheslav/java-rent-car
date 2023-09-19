package com.dmdev.dto;

import com.dmdev.entity.Role;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
public class UserUpdateDto {

    @NotBlank(message = "Email is required")
    @Email
    String email;

    @NotBlank(message = "Username is required")
    @Size(min = 2, message = "Username should have at least 2 characters")
    String username;

    Role role;
}