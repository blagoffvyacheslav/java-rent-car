package com.dmdev.dto;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Value
public class UserChangePasswordDto {

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String currentPassword;

    @NotEmpty
    @Size(min = 8, message = "Password should have at least 8 characters")
    String newPassword;
}