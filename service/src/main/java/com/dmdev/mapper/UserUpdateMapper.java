package com.dmdev.mapper;

import com.dmdev.dto.UserUpdateDto;
import com.dmdev.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper implements Mapper<UserUpdateDto, User> {

    @Override
    public User map(UserUpdateDto object) {
        return User.builder()
                .email(object.getEmail())
                .login(object.getLogin())
                .build();
    }

    @Override
    public User map(UserUpdateDto requestDto, User existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(UserUpdateDto requestDto, User existing) {
        existing.setLogin(requestDto.getLogin());
        existing.setEmail(requestDto.getEmail());
        existing.setRole(requestDto.getRole());
    }
}