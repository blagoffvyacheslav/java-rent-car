package com.dmdev.mapper;

import com.dmdev.dto.UserDetailsCreateDto;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class UserDetailsCreateMapper implements Mapper<UserDetailsCreateDto, UserDetails> {

    @Override
    public UserDetails map(UserDetailsCreateDto requestDto) {
        return UserDetails.builder()
                .user(User.builder()
                        .id(requestDto.getUserId())
                        .build())
                .name(requestDto.getName())
                .lastname(requestDto.getLastname())
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .birthday(requestDto.getBirthday())
                .build();
    }
}