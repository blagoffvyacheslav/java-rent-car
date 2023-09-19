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
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.predicate.UserDetailsPredicateBuilder;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserRepository userRepository;
    private final UserDetailsCreateMapper userDetailsCreateMapper;
    private final UserDetailsUpdateMapper userDetailsUpdateMapper;
    private final UserDetailsReadMapper userDetailsReadMapper;
    private final UserDetailsPredicateBuilder userDetailsPredicateBuilder;

    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));

        if(user.isPresent()){
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority(user.get().getRole().toString())
            );
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    authorities
            );
        }

        throw new UsernameNotFoundException("empty or invalud user");
    }

    @Transactional
    public UserDetailsReadDto create(UserDetailsCreateDto userDetailsCreateDto) {
        var existingUser = getUserByIdOrElseThrow(userDetailsCreateDto.getUserId());
        var userDetails = userDetailsCreateMapper.map(userDetailsCreateDto);
        userDetails.setUser(existingUser);

        var savedEntity = userDetailsRepository.save(userDetails);

        return userDetailsReadMapper.map(savedEntity);
    }

    @Transactional
    public UserDetailsReadDto update(Long id, UserDetailsUpdateDto userDetails) {
        var existingUserDetails = getByIdOrElseThrow(id);

        var entity = userDetailsUpdateMapper.map(userDetails, existingUserDetails);
        var savedEntity = userDetailsRepository.save(entity);
        return userDetailsReadMapper.map(savedEntity);
    }

    @Transactional(readOnly = true)
    public UserDetailsReadDto getById(Long id) {
        return userDetailsReadMapper.map(getByIdOrElseThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<UserDetailsReadDto> getAll(UserDetailsFilterDto userDetailsFilterDto, Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "lastname");
        return userDetailsRepository.findAll(
                        (userDetailsPredicateBuilder.build(userDetailsFilterDto)), pageRequest)
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
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("User", "id", id)));
    }

    private UserDetails getByIdOrElseThrow(Long id) {
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("User", "id", id)));

    }
}