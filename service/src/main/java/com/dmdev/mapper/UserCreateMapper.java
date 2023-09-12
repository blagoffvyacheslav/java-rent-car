package com.dmdev.mapper;

import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final UserDetailsCreateMapper userDetailsCreateMapper;
    private final DriverLicenseCreateMapper driverLicenseCreateMapper;
    @Override
    public User map(UserCreateDto createDto) {
        var driverLicense = driverLicenseCreateMapper.map(createDto);
        var userDetails = userDetailsCreateMapper.map(createDto);
        var user = User.builder()
                .login(createDto.getLogin())
                .email(createDto.getEmail())
                .role(createDto.getRole())
                .password(PasswordUtil.hashPassword(createDto.getPassword()))
                .build();
        userDetails.setUser(user);
        userDetails.setDriverLicense(driverLicense);
        return user;
    }
}