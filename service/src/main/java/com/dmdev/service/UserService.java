package com.dmdev.service;

import com.dmdev.dto.*;
import com.dmdev.dto.UserReadDto;
import com.dmdev.entity.User;
import com.dmdev.entity.Role;
import com.dmdev.mapper.UserCreateMapper;
import com.dmdev.mapper.UserReadMapper;
import com.dmdev.mapper.UserUpdateMapper;
import com.dmdev.repository.UserRepository;
//import com.dmdev.utils.PageableUtils;
import com.dmdev.utils.PasswordUtil;
import com.dmdev.utils.UserPredicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;
    private final UserUpdateMapper userUpdateMapper;
    private final UserReadMapper userResponseMapper;
//    private final UserPredicateBuilder userPredicateBuilder;

    @Transactional
    public Optional<UserReadDto> create(UserCreateDto userRequestDto) {
        this.checkUsernameIsUnique(userRequestDto.getUsername());
        this.checkEmailIsUnique(userRequestDto.getEmail());

        return Optional.of(userCreateMapper.map(userRequestDto))
                .map(userRepository::save)
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> login(LoginDto loginDto) {
        return userRepository.findByUsernameAndPassword(loginDto.getUsername(),
                        PasswordUtil.hashPassword(loginDto.getPassword()))
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> update(Long id, UserUpdateDto user) {
        var existingUser = getByIdOrElseThrow(id);

        if (!existingUser.getEmail().equals(user.getEmail())) {
            checkUsernameIsUnique(user.getEmail());
        }

        return Optional.of(userUpdateMapper.map(user, existingUser))
                .map(userRepository::save)
                .map(userResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<UserReadDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> changePassword(Long id, UserChangePasswordDto changedPasswordDto) {
        var existingUser = getByIdOrElseThrow(id);

        if (isExistsByUsernameAndPassword(existingUser.getEmail(),
                PasswordUtil.hashPassword(changedPasswordDto.getCurrentPassword()))) {
            existingUser.setPassword(
                    PasswordUtil.hashPassword(changedPasswordDto.getNewPassword())
            );
        }

        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::map);
    }

    @Transactional
    public Optional<UserReadDto> changeRole(Long id, Role role) {
        var existingUser = getByIdOrElseThrow(id);
        Optional.of(role).ifPresent(existingUser::setRole);
        return Optional.of(userRepository.save(existingUser))
                .map(userResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserReadDto> getAll(UserFilterDto userFilterDto, Integer page, Integer pageSize) {
        return userRepository.findAll(UserPredicate.build(userFilterDto), PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "userDetails_lastname")).map(userResponseMapper::map);
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void checkUsernameIsUnique(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    public void checkEmailIsUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isExistsByUsernameAndPassword(String email, String password) {
        return userRepository.existsByUsernameAndPassword(email, password);
    }
}