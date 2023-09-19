package com.dmdev.mapper;

import com.dmdev.dto.DriverLicenseCreateDto;
import com.dmdev.entity.DriverLicense;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DriverLicenseCreateMapper implements Mapper<DriverLicenseCreateDto, DriverLicense> {

    @Override
    public DriverLicense map(DriverLicenseCreateDto requestDto) {
        return DriverLicense.builder()
                .number(requestDto.getLicenseNumber())
                .issueDate(requestDto.getIssueDate())
                .expiredDate(requestDto.getExpiredDate())
                .userDetails(UserDetails.builder()
                        .user(User.builder()
                                .id(requestDto.getUserId())
                                .build())
                        .build())
                .build();
    }
}