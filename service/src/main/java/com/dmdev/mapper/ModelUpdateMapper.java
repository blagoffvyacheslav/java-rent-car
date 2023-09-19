package com.dmdev.mapper;

import com.dmdev.entity.Model;
import com.dmdev.dto.ModelUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModelUpdateMapper implements Mapper<ModelUpdateDto, Model> {

    @Override
    public Model map(ModelUpdateDto object) {
        return null;
    }

    @Override
    public Model map(ModelUpdateDto requestDto, Model existing) {
        merge(requestDto, existing);
        return existing;
    }

    public void merge(ModelUpdateDto requestDto, Model existing) {
        existing.setName(requestDto.getName());
    }
}