package com.dmdev.mapper;

import com.dmdev.dto.UserUpdateDto;
import com.dmdev.entity.User;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper implements Mapper<UserUpdateDto, User> {

    @Override
    public User map(UserUpdateDto object) {
        return User.builder()
                .email(object.getEmail())
                .username(object.getUsername())
                .build();
    }

    @Override
    public User map(UserUpdateDto requestDto, User existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(UserUpdateDto requestDto, User existing) {
        existing.setUsername(requestDto.getUsername());
        existing.setEmail(requestDto.getEmail());
        existing.setRole(requestDto.getRole());
    }
}