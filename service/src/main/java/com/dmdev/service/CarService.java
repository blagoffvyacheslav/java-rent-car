package com.dmdev.service;

import com.dmdev.entity.Car;
import com.dmdev.dto.CarCreateDto;
import com.dmdev.dto.CarFilterDto;
import com.dmdev.dto.CarReadDto;
import com.dmdev.dto.CarUpdateDto;
import com.dmdev.mapper.CarReadMapper;
import com.dmdev.mapper.CarCreateMapper;
import com.dmdev.mapper.CarUpdateMapper;
import com.dmdev.repository.CarRepository;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.predicate.CarPredicateBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarCreateMapper carCreateMapper;
    private final CarUpdateMapper carUpdateMapper;
    private final CarReadMapper carReadMapper;
    private final ImageService imageService;
    private final CarPredicateBuilder carPredicateBuilder;

    @Transactional
    public CarReadDto create(CarCreateDto carCreateDto) {
        if (carCreateDto.getImage() != null) {
            downloadImage(carCreateDto.getImage());
        }

        var entity = carCreateMapper.map(carCreateDto);
        var savedEntity = carRepository.save(entity);

        return carReadMapper.map(savedEntity);
    }

    @Transactional
    public CarReadDto update(Long id, CarUpdateDto carUpdateDto) {
        var existingCar = getByIdOrElseThrow(id);

        if (carUpdateDto.getImage() != null) {
            downloadImage(carUpdateDto.getImage());
        }

        var entity = carUpdateMapper.map(carUpdateDto, existingCar);
        var savedEntity = carRepository.save(entity);

        return carReadMapper.map(savedEntity);
    }

    public CarReadDto getById(Long id) {
        return carReadMapper.map(getByIdOrElseThrow(id));
    }

    public List<CarReadDto> getAll() {
        return carRepository.findAll().stream()
                .map(carReadMapper::map)
                .collect(toList());
    }

    public Page<CarReadDto> getAll(Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "serialNumber");
        return carRepository.findAll(pageRequest)
                .map(carReadMapper::map);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public CarReadDto getByCarSerialNumber(String serialNumber) {
        var car = carRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("Car", "serialNumber", serialNumber)));
        return carReadMapper.map(car);
    }

    public List<CarReadDto> getAllWithDamages() {
        return carRepository.findAllWithDamages()
                .stream()
                .map(carReadMapper::map)
                .collect(toList());
    }

    public List<CarReadDto> getAllWithoutDamages() {
        return carRepository.findAllWithoutDamages()
                .stream()
                .map(carReadMapper::map)
                .collect(toList());
    }

    public List<CarReadDto> getAllIsNew() {
        return carRepository.findAllIsNew()
                .stream()
                .map(carReadMapper::map)
                .collect(toList());
    }

    public boolean isCarAvailable(Long id, LocalDate startDate, LocalDate endDate) {
        return carRepository.isCarAvailable(id, startDate, endDate);
    }

    public Page<CarReadDto> getAll(CarFilterDto carFilter, Integer page, Integer pageSize) {
        return carRepository.findAll(carPredicateBuilder.build(carFilter), PageRequest.of(page, pageSize).withSort(Sort.Direction.ASC, "brand_name")).map(carReadMapper::map);
    }

    public Optional<byte[]> findCarImage(Long id) {
        return carRepository.findById(id)
                .map(Car::getImage)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    private Car getByIdOrElseThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("Car", "id", id)));
    }

    @SneakyThrows
    private void downloadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.upload(image.getOriginalFilename(), image.getInputStream());
        }
    }
}