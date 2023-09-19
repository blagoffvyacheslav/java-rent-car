package com.dmdev.dto;

import com.dmdev.entity.Role;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class UserReadDto {

    @NotEmpty
    Long id;

    String username;
    String email;
    Role role;
    UserDetailsReadDto userDetailsDto;
    DriverLicenseReadDto driverLicenseDto;
}