package com.dmdev.utils.predicate;

import com.dmdev.dto.CarFilterDto;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static com.dmdev.entity.QCar.car;

@Component
public class CarPredicateBuilder implements PredicateBuilder<CarFilterDto> {

    @Override
    public Predicate build(CarFilterDto requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getIsNew(), car.isNew::eq)
                .add(requestFilter.getSerialNumber(), car.serialNumber::eq)
                .add(requestFilter.getModelNames(), car.model.name::in)
                .buildAnd();
    }
}