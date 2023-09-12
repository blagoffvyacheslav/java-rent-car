package com.dmdev.mapper;

import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseFromUserCreateMapper implements Mapper<UserCreateDto, DriverLicense> {

    @Override
    public DriverLicense map(UserCreateDto requestDto) {
        return DriverLicense.builder()
                .number(requestDto.getDriverLicenseNumber())
                .issueDate(requestDto.getDriverLicenseIssueDate())
                .expiredDate(requestDto.getDriverLicenseExpiredDate())
                .build();
    }
}