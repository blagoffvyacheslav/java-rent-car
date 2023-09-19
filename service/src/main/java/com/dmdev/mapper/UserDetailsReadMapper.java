package com.dmdev.mapper;

import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.entity.UserDetails;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsReadMapper implements Mapper<UserDetails, UserDetailsReadDto> {

    @Override
    public UserDetailsReadDto map(UserDetails userDetails) {
        return UserDetailsReadDto.builder()
                .id(userDetails.getId())
                .userId(userDetails.getUser().getId())
                .name(userDetails.getName())
                .lastname(userDetails.getLastname())
                .address(userDetails.getAddress())
                .phone(userDetails.getPhone())
                .birthday(userDetails.getBirthday())
                .registrationAt(userDetails.getRegistrationDate())
                .build();
    }
}