package com.dmdev.service;

import com.dmdev.dto.DriverLicenseCreateDto;
import com.dmdev.dto.DriverLicenseUpdateDto;
import com.dmdev.dto.DriverLicenseReadDto;
import com.dmdev.entity.DriverLicense;
import com.dmdev.mapper.DriverLicenseCreateMapper;
import com.dmdev.mapper.DriverLicenseReadMapper;
import com.dmdev.mapper.DriverLicenseUpdateMapper;
import com.dmdev.repository.DriverLicenseRepository;
import com.dmdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverLicenseService {

    private final DriverLicenseRepository driverLicenseRepository;
    private final UserRepository userRepository;
    private final DriverLicenseCreateMapper driverLicenseCreateMapper;
    private final DriverLicenseUpdateMapper driverLicenseUpdateMapper;
    private final DriverLicenseReadMapper driverLicenseReadMapper;

    @Transactional
    public Optional<DriverLicenseReadDto> create(DriverLicenseCreateDto driverLicenseCreateRequestDto) {
        var optionalUser = userRepository.findById(driverLicenseCreateRequestDto.getUserId());
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            var user = optionalUser.get();
            checkDriverLicenseNumberIsUnique(driverLicenseCreateRequestDto.getLicenseNumber());
            var driverLicense = driverLicenseCreateMapper.map(driverLicenseCreateRequestDto);
            user.getUserDetails().setDriverLicense(driverLicense);

            return Optional.of(driverLicense)
                    .map(driverLicenseRepository::save)
                    .map(driverLicenseReadMapper::map);
        }
    }

    @Transactional
    public Optional<DriverLicenseReadDto> update(Long id, DriverLicenseUpdateDto driverLicenseUpdateRequestDto) {
        var existingDriverLicense = getUserByIdOrElseThrow(id);

        if (!existingDriverLicense.getNumber().equals(driverLicenseUpdateRequestDto.getDriverLicenseNumber())) {
            checkDriverLicenseNumberIsUnique(driverLicenseUpdateRequestDto.getDriverLicenseNumber());
        }

        return Optional.of(driverLicenseUpdateMapper.map(driverLicenseUpdateRequestDto, existingDriverLicense))
                .map(driverLicenseRepository::save)
                .map(driverLicenseReadMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<DriverLicenseReadDto> getById(Long id) {
        return Optional.of(getUserByIdOrElseThrow(id))
                .map(driverLicenseReadMapper::map);
    }

    @Transactional(readOnly = true)
    public Page<DriverLicenseReadDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "number");
        return driverLicenseRepository.findAll(pageRequest)
                .map(driverLicenseReadMapper::map);
    }

    @Transactional(readOnly = true)
    public List<DriverLicenseReadDto> getByNumber(String number) {
        return driverLicenseRepository.findByNumberContainingIgnoreCase(number).stream()
                .map(driverLicenseReadMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DriverLicenseReadDto> getByUserId(Long userId) {
        return driverLicenseRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(DriverLicense::getExpiredDate).reversed())
                .findFirst()
                .map(driverLicenseReadMapper::map);
    }

    @Transactional(readOnly = true)
    public List<DriverLicenseReadDto> getAllExpiredDriverLicenses() {
        return driverLicenseRepository.findByExpiredDateLessThanEqual(LocalDate.now()).stream()
                .sorted(Comparator.comparing(DriverLicense::getExpiredDate).reversed())
                .map(driverLicenseReadMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (driverLicenseRepository.existsById(id)) {
            driverLicenseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private DriverLicense getUserByIdOrElseThrow(Long id) {
        return driverLicenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    private void checkDriverLicenseNumberIsUnique(String licenseNumber) {
        if (driverLicenseRepository.existsByNumber(licenseNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}