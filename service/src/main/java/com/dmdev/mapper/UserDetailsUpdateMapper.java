package com.dmdev.mapper;

import com.dmdev.dto.UserDetailsUpdateDto;
import com.dmdev.entity.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsUpdateMapper implements Mapper<UserDetailsUpdateDto, UserDetails> {

    @Override
    public UserDetails map(UserDetailsUpdateDto object) {
        return UserDetails.builder()
                .name(object.getName())
                .lastname(object.getLastname())
                .address(object.getAddress())
                .phone(object.getPhone())
                .build();
    }

    @Override
    public UserDetails map(UserDetailsUpdateDto requestDto, UserDetails existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(UserDetailsUpdateDto requestDto, UserDetails existing) {
        existing.setName(requestDto.getName());
        existing.setLastname(requestDto.getLastname());
        existing.setPhone(requestDto.getPhone());
        existing.setAddress(requestDto.getAddress());
    }
}