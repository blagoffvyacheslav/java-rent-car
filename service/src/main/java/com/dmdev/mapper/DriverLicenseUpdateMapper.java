package com.dmdev.mapper;

import com.dmdev.dto.DriverLicenseUpdateDto;
import com.dmdev.entity.DriverLicense;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseUpdateMapper implements Mapper<DriverLicenseUpdateDto, DriverLicense> {

    @Override
    public DriverLicense map(DriverLicenseUpdateDto object) {
        return DriverLicense.builder()
                .number(object.getDriverLicenseNumber())
                .issueDate(object.getDriverLicenseIssueDate())
                .expiredDate(object.getDriverLicenseExpiredDate())
                .build();
    }

    @Override
    public DriverLicense map(DriverLicenseUpdateDto requestDto, DriverLicense existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(DriverLicenseUpdateDto requestDto, DriverLicense existing) {
        existing.setNumber(requestDto.getDriverLicenseNumber());
        existing.setIssueDate(requestDto.getDriverLicenseIssueDate());
        existing.setExpiredDate(requestDto.getDriverLicenseExpiredDate());
    }
}