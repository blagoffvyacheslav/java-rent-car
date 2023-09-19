package com.dmdev.mapper;

import com.dmdev.dto.CarReadDto;
import com.dmdev.entity.Car;
import com.dmdev.entity.Model;
import com.dmdev.entity.CarRate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CarReadMapper implements Mapper<Car, CarReadDto> {

    @Override
    public CarReadDto map(Car car) {
        Set<BigDecimal> prices = Optional.ofNullable(car)
                .map(Car::getModel)
                .map(Model::getCarRates)
                .map(carRates -> carRates.stream()
                        .map(CarRate::getPrice)
                        .collect(Collectors.toSet()))
                .orElseGet(Collections::emptySet);
        return CarReadDto.builder()
                .id(car.getId())
                .model(car.getModel().getName())
                .serialNumber(car.getSerialNumber())
                .isNew(car.getIsNew())
                .image(car.getImage())
                .prices(prices)
                .build();
    }
}