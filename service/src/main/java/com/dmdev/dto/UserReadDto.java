package com.dmdev.dto;

import com.dmdev.dto.DriverLicenseReadDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.entity.Role;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class UserReadDto {

    @NotEmpty
    long id;
    String username;
    String email;
    Role role;
    UserDetailsReadDto userDetailsDto;
    DriverLicenseReadDto driverLicenseDto;
}