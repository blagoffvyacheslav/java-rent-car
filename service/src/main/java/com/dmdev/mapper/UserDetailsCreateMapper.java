package com.dmdev.mapper;

import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsCreateMapper implements Mapper<UserCreateDto, UserDetails> {

    @Override
    public UserDetails map(UserCreateDto createDto) {
        return UserDetails.builder()
                .name(createDto.getName())
                .lastname(createDto.getLastname())
                .phone(createDto.getPhone())
                .passportNumber(createDto.getPassportNumber())
                .address(createDto.getAddress())
                .birthday(createDto.getBirthday())
                .build();
    }
}