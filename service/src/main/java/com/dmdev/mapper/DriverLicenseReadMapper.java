package com.dmdev.mapper;

import com.dmdev.dto.DriverLicenseReadDto;
import com.dmdev.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseReadMapper implements Mapper<DriverLicense, DriverLicenseReadDto> {

    @Override
    public DriverLicenseReadDto map(DriverLicense driverLicense) {
        return DriverLicenseReadDto.builder()
                .id(driverLicense.getId())
                .userId(driverLicense.getUserDetails().getUser().getId())
                .licenseNumber(driverLicense.getNumber())
                .issueDate(driverLicense.getIssueDate())
                .expiredDate(driverLicense.getExpiredDate())
                .build();
    }
}