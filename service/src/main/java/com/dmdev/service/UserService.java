package com.dmdev.service;

import com.dmdev.dto.LoginDto;
import com.dmdev.dto.UserChangePasswordDto;
import com.dmdev.dto.UserCreateDto;
import com.dmdev.dto.UserUpdateDto;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.User;
import com.dmdev.mapper.UserCreateMapper;
import com.dmdev.mapper.UserReadMapper;
import com.dmdev.mapper.UserUpdateMapper;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
//@EnableTransactionManagement
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserReadMapper userReadMapper;

    @Transactional
    public Optional<UserReadDto> create(UserCreateDto userCreateDto) {
        this.checkEmailIsUnique(userCreateDto.getEmail());

        var user = userCreateMapper.map(userCreateDto);
        return Optional.of(userReadMapper.map(userRepository.save(user)));
    }

    @Transactional
    public Optional<UserReadDto> login(LoginDto loginRequestDto) {
        return userRepository.findByEmailAndPassword(loginRequestDto.getEmail(),
                        loginRequestDto.getPassword())
                .map(userReadMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserUpdateDto user) {
        var existingUser = getByIdOrElseThrow(id);

        if (!existingUser.getEmail().equals(user.getEmail())) {
            checkEmailIsUnique(user.getEmail());
        }

        return Optional.of(
                        userRepository.save(
                                userUpdateMapper.map(user, existingUser)))
                .map(userReadMapper::map);
    }
    @Transactional(readOnly = true)
    public Optional<UserReadDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userReadMapper::map);
    }

    public Optional<UserReadDto> changePassword(Long id, UserChangePasswordDto changedPasswordDto) {
        var existingUser = getByIdOrElseThrow(id);

        if (isExistsByEmailAndPassword(existingUser.getEmail(),
                PasswordUtil.hashPassword(changedPasswordDto.getCurrentPassword()))) {
            existingUser.setPassword(
                    PasswordUtil.hashPassword(changedPasswordDto.getNewPassword())
            );
        }

        return Optional.of(userRepository.save(existingUser))
                .map(userReadMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserReadDto> getAll(Integer page, Integer pageSize) {
        Pageable pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "userDetails_lastname");
        return userRepository.findAll(pageRequest)
                .map(userReadMapper::map);
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public void checkEmailIsUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, (String.format("User with email '%s' already exists", email)));
        }
    }

    private boolean isExistsByEmailAndPassword(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }
}