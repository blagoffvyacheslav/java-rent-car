package com.dmdev.mapper;

import com.dmdev.dto.DriverLicenseCreateDto;
import com.dmdev.entity.DriverLicense;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.http.controller.UserController;
import com.dmdev.mapper.Mapper;
import com.dmdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class DriverLicenseCreateMapper implements Mapper<DriverLicenseCreateDto, DriverLicense> {

    private final UserRepository userRepository;

    @Override
    public DriverLicense map(DriverLicenseCreateDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDto.getUserId()));
        return DriverLicense.builder()
                .number(requestDto.getLicenseNumber())
                .issueDate(requestDto.getIssueDate())
                .expiredDate(requestDto.getExpiredDate())
                .userDetails(UserDetails.builder()
                        .user(User.builder()
                                .id(user.getId())
                                .build())
                        .build())
                .build();
    }
}