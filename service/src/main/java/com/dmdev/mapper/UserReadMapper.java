package com.dmdev.mapper;

import com.dmdev.dto.DriverLicenseReadDto;
import com.dmdev.dto.UserReadDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserReadMapper implements Mapper<User, UserReadDto> {

    @Override
    public UserReadDto map(User user) {
        return UserReadDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .userDetailsDto(UserDetailsReadDto.builder()
                        .id(user.getUserDetails().getId())
                        .userId(user.getId())
                        .name(user.getUserDetails().getName())
                        .lastname(user.getUserDetails().getLastname())
                        .address(user.getUserDetails().getAddress())
                        .phone(user.getUserDetails().getPhone())
                        .birthday(user.getUserDetails().getBirthday())
                        .registrationAt(user.getUserDetails().getRegistrationDate())
                        .build())
                .role(user.getRole())
                .driverLicenseDto(DriverLicenseReadDto.builder()
                        .id(user.getUserDetails().getDriverLicenses().iterator().next().getId())
                        .userId(user.getId())
                        .licenseNumber(user.getUserDetails().getDriverLicenses().iterator().next().getNumber())
                        .issueDate(user.getUserDetails().getDriverLicenses().iterator().next().getIssueDate())
                        .expiredDate(user.getUserDetails().getDriverLicenses().iterator().next().getExpiredDate())
                        .build())
                .build();
    }
}