package com.dmdev.mapper;

import com.dmdev.dto.UserDetailsCreateDto;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;


@Component
@RequiredArgsConstructor
public class UserDetailsCreateMapper implements Mapper<UserDetailsCreateDto, UserDetails> {

    private final UserRepository userRepository;

    @Override
    public UserDetails map(UserDetailsCreateDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDto.getUserId()));
        return UserDetails.builder()
                .user(User.builder()
                        .id(user.getId())
                        .build())
                .name(requestDto.getName())
                .lastname(requestDto.getLastname())
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .birthday(requestDto.getBirthday())
                .build();
    }
}