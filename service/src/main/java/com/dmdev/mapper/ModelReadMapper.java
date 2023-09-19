package com.dmdev.mapper;

import com.dmdev.dto.ModelReadDto;
import com.dmdev.entity.Model;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelReadMapper implements Mapper<Model, ModelReadDto> {

    @Override
    public ModelReadDto map(Model model) {
        return ModelReadDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

//    public Set<ModelReadDto> mapToDtos(Set<Model> models) {
//        return models.stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toSet());
//    }
}