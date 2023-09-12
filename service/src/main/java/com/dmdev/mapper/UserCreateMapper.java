package com.dmdev.mapper;

import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.User;
import com.dmdev.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final UserDetailsFromUserCreateMapper userDetailsCreateMapper;
    private final DriverLicenseFromUserCreateMapper driverLicenseCreateMapper;

    @Override
    public User map(UserCreateDto requestDto) {
        var driverLicense = driverLicenseCreateMapper.map(requestDto);
        var userDetails = userDetailsCreateMapper.map(requestDto);
        userDetails.setPassportNumber(requestDto.getPassportNumber());
        var user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .role(requestDto.getRole())
                .password(PasswordUtil.hashPassword(requestDto.getPassword()))
                .build();
        userDetails.setUser(user);
        userDetails.setDriverLicense(driverLicense);
        return user;
    }
}