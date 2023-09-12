package com.dmdev.mapper;

import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;


@Component
public class DriverLicenseCreateMapper implements Mapper<UserCreateDto, DriverLicense> {

    @Override
    public DriverLicense map(UserCreateDto createDto) {
        return DriverLicense.builder()
                .number(createDto.getDriverLicenseNumber())
                .issueDate(createDto.getDriverLicenseIssueDate())
                .expiredDate(createDto.getDriverLicenseExpiredDate())
                .build();
    }
}