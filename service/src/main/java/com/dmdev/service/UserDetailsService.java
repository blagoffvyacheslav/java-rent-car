package com.dmdev.service;

import com.dmdev.dto.UserDetailsCreateDto;
import com.dmdev.dto.UserDetailsFilterDto;
import com.dmdev.dto.UserDetailsUpdateDto;
import com.dmdev.dto.UserDetailsReadDto;
import com.dmdev.entity.User;
import com.dmdev.entity.UserDetails;
import com.dmdev.mapper.UserDetailsCreateMapper;
import com.dmdev.mapper.UserDetailsReadMapper;
import com.dmdev.mapper.UserDetailsUpdateMapper;
import com.dmdev.repository.UserDetailsRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.QPredicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dmdev.entity.QUserDetails.userDetails;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final UserDetailsCreateMapper userDetailsCreateMapper;
    private final UserDetailsUpdateMapper userDetailsUpdateMapper;
    private final UserDetailsReadMapper userDetailsReadMapper;

    @Transactional
    public Optional<UserDetailsReadDto> create(UserDetailsCreateDto userDetailsCreateDto) {
        var existingUser = getUserByIdOrElseThrow(userDetailsCreateDto.getUserId());
        var userDetails = userDetailsCreateMapper.map(userDetailsCreateDto);
        userDetails.setUser(existingUser);

        return Optional.of(userDetails)
                .map(userDetailsRepository::save)
                .map(userDetailsReadMapper::map);

    }

    @Transactional
    public Optional<UserDetailsReadDto> update(Long id, UserDetailsUpdateDto userDetails) {
        var existingUserDetails = getByIdOrElseThrow(id);

        return Optional.of(userDetailsUpdateMapper.map(userDetails, existingUserDetails))
                .map(userDetailsRepository::save)
                .map(userDetailsReadMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<UserDetailsReadDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(userDetailsReadMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<UserDetailsReadDto> getAll(UserDetailsFilterDto userDetailsFilterDto, Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "lastname");
        return userDetailsRepository.findAll(
                        (QPredicate.builder()
                                .add(userDetailsFilterDto.getName(), userDetails.name::containsIgnoreCase)
                                .add(userDetailsFilterDto.getLastname(), userDetails.lastname::containsIgnoreCase)
                                .add(userDetailsFilterDto.getBirthday(), userDetails.birthday::eq)
                                .add(userDetailsFilterDto.getPhone(), userDetails.phone::eq)
                                .add(userDetailsFilterDto.getAddress(), userDetails.address::containsIgnoreCase)
                                .buildAnd()), pageRequest)
                .map(userDetailsReadMapper::map);
    }

    @Transactional(readOnly = true)
    public List<UserDetailsReadDto> getAllByNameAndLastname(String name, String lastname) {
        return userDetailsRepository.findAllByNameContainingIgnoreCaseAndLastnameContainingIgnoreCase(name, lastname).stream()
                .map(userDetailsReadMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDetailsReadDto> getAllByRegistrationDate(LocalDate registrationDate) {
        return userDetailsRepository.findByRegistrationDate(registrationDate).stream()
                .map(userDetailsReadMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDetailsReadDto> getAllByRegistrationDates(LocalDate start, LocalDate end) {
        return userDetailsRepository.findByRegistrationDateBetween(start, end).stream()
                .map(userDetailsReadMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDetailsReadDto> getByUserId(Long userId) {
        return Optional.of(userDetailsRepository.findByUserId(userId))
                .map(userDetailsReadMapper::map);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (userDetailsRepository.existsById(id)) {
            userDetailsRepository.deleteById(id);
            return true;
        }
        return false;
    }


    private User getUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private UserDetails getByIdOrElseThrow(Long id) {
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }
}