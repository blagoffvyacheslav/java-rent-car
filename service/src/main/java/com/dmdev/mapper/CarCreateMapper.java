package com.dmdev.mapper;

import com.dmdev.dto.CarCreateDto;
import com.dmdev.entity.Car;
import com.dmdev.entity.Model;
import com.dmdev.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class CarCreateMapper implements Mapper<CarCreateDto, Car> {

    private final ModelRepository modelRepository;

    @Override
    public Car map(CarCreateDto requestDto) {
        var car = Car.builder()
                .serialNumber(requestDto.getSerialNumber())
                .isNew(requestDto.getIsNew())
                .build();

        car.setModel(getModel(requestDto.getModelId()));

        Optional.ofNullable(requestDto.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> car.setImage(image.getOriginalFilename()));

        return car;
    }

    private Model getModel(Long modelId) {
        return Optional.ofNullable(modelId)
                .flatMap(modelRepository::findById)
                .orElse(null);
    }
}