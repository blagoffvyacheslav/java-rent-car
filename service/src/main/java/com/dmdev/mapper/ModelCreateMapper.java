package com.dmdev.mapper;

import com.dmdev.dto.ModelCreateDto;
import com.dmdev.entity.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ModelCreateMapper implements Mapper<ModelCreateDto, Model> {

    @Override
    public Model map(ModelCreateDto requestDto) {
        var model = Model.builder()
                .name(requestDto.getName())
                .build();
        return model;
    }
}