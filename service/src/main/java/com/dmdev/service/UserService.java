package com.dmdev.service;

import com.dmdev.dto.*;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.User;
import com.dmdev.entity.Role;
import com.dmdev.mapper.UserCreateMapper;
import com.dmdev.mapper.UserReadMapper;
import com.dmdev.mapper.UserUpdateMapper;
import com.dmdev.repository.UserRepository;
import com.dmdev.service.exception.BadRequestException;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.predicate.UserPredicateBuilder;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserReadMapper userResponseMapper;
    private final UserPredicateBuilder userPredicateBuilder;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserReadDto create(UserCreateDto userRequestDto) {
        this.checkUsernameIsUnique(userRequestDto.getUsername());
        this.checkEmailIsUnique(userRequestDto.getEmail());

        var entity = userCreateMapper.map(userRequestDto);
        var savedEntity = userRepository.save(entity);
        return userResponseMapper.map(savedEntity);
    }

    @Transactional
    public UserReadDto update(Long id, UserUpdateDto user) {
        var existingUser = getByIdOrElseThrow(id);

        if (!existingUser.getEmail().equals(user.getEmail())) {
            checkUsernameIsUnique(user.getEmail());
        }

        var entity = userUpdateMapper.map(user, existingUser);
        var savedEntity = userRepository.save(entity);

        return userResponseMapper.map(savedEntity);
    }

    @Transactional(readOnly = true)
    public UserReadDto getById(Long id) {
        return userResponseMapper.map(getByIdOrElseThrow(id));
    }

    @Transactional
    public UserReadDto changePassword(Long id, UserChangePasswordDto changedPasswordDto) {
        var existingUser = getByIdOrElseThrow(id);

        if (isExistsByUsernameAndPassword(existingUser.getEmail(),
                passwordEncoder.encode(changedPasswordDto.getCurrentPassword()))) {
            existingUser.setPassword(
                    passwordEncoder.encode(changedPasswordDto.getNewPassword())
            );
        }
        var savedEntity = userRepository.save(existingUser);
        return userResponseMapper.map(savedEntity);
    }

    @Transactional
    public UserReadDto changeRole(Long id, Role role) {
        var existingUser = getByIdOrElseThrow(id);
        Optional.of(role).ifPresent(existingUser::setRole);
        var savedEntity = userRepository.save(existingUser);
        return userResponseMapper.map(savedEntity);
    }

    @Transactional(readOnly = true)
    public Page<UserReadDto> getAll(UserFilterDto userFilterDto, Integer page, Integer pageSize) {
        return userRepository.findAll(userPredicateBuilder.build(userFilterDto), PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "userDetails_lastname")).map(userResponseMapper::map);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private User getByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("User", "id", id)));
    }

    public void checkUsernameIsUnique(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException(String.format(BadRequestException.existsPrepare("User", "username", username)));
        }
    }
    public void checkEmailIsUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException(String.format(BadRequestException.existsPrepare("User", "email", email)));
        }
    }

    private boolean isExistsByUsernameAndPassword(String email, String password) {
        return userRepository.existsByUsernameAndPassword(email, password);
    }
}