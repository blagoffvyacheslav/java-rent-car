package com.dmdev.mapper;

import com.dmdev.entity.Car;
import com.dmdev.entity.Model;
import com.dmdev.dto.CarUpdateDto;
import com.dmdev.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
public class CarUpdateMapper implements Mapper<CarUpdateDto, Car> {

    private final ModelRepository modelRepository;

    @Override
    public Car map(CarUpdateDto requestDto) {
        Car car = Car.builder()
                .model(getModel(requestDto.getModelId()))
                .serialNumber(requestDto.getSerialNumber())
                .isNew(requestDto.getIsNew()).build();
        merge(requestDto, car);
        return car;
    }

    @Override
    public Car map(CarUpdateDto requestDto, Car car) {
        merge(requestDto, car);
        return car;
    }

    public void merge(CarUpdateDto requestDto, Car car) {
        car.setSerialNumber(requestDto.getSerialNumber());
        car.setIsNew(requestDto.getIsNew());

        if (!Objects.equals(requestDto.getModelId(), car.getModel().getId())) {
            var model = getModel(requestDto.getModelId());
            car.setModel(model);
        }

        Optional.ofNullable(requestDto.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> car.setImage(image.getOriginalFilename()));
    }

    private Model getModel(Long modelId) {
        return Optional.ofNullable(modelId)
                .flatMap(modelRepository::findById)
                .orElse(null);
    }

    private static void processImage(CarUpdateDto requestDto, MultipartFile multipartFile, Car car){
        Optional.ofNullable(requestDto.getImage())
                .filter(not(MultipartFile::isEmpty))
                .ifPresent(image -> car.setImage(image.getOriginalFilename()));
    }
}