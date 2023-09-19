package com.dmdev.utils.predicate;

import com.dmdev.dto.ModelFilterDto;
import com.querydsl.core.types.Predicate;

import org.springframework.stereotype.Component;

import static com.dmdev.entity.QModel.model;

@Component
public class ModelPredicateBuilder implements PredicateBuilder<ModelFilterDto> {

    @Override
    public Predicate build(ModelFilterDto requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getModels(), model.name::in)
                .buildAnd();
    }
}