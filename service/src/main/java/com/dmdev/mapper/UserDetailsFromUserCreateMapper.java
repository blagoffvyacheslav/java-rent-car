package com.dmdev.mapper;

import com.dmdev.dto.UserCreateDto;
import com.dmdev.entity.UserDetails;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsFromUserCreateMapper implements Mapper<UserCreateDto, UserDetails> {

    @Override
    public UserDetails map(UserCreateDto requestDto) {
        return UserDetails.builder()
                .name(requestDto.getName())
                .lastname(requestDto.getLastname())
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .birthday(requestDto.getBirthday())
                .build();
    }
}