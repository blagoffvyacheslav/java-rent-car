package com.dmdev.service;

import com.dmdev.dto.ModelCreateDto;
import com.dmdev.dto.ModelFilterDto;
import com.dmdev.dto.ModelUpdateDto;
import com.dmdev.entity.Model;
import com.dmdev.dto.ModelReadDto;
import com.dmdev.mapper.ModelCreateMapper;
import com.dmdev.mapper.ModelReadMapper;
import com.dmdev.mapper.ModelUpdateMapper;
import com.dmdev.repository.ModelRepository;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.utils.predicate.ModelPredicateBuilder;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelCreateMapper modelCreateMapper;
    private final ModelUpdateMapper modelUpdateMapper;
    private final ModelReadMapper modelReadMapper;
    private final ModelPredicateBuilder modelPredicateBuilder;

    @Transactional
    public ModelReadDto create(ModelCreateDto modelCreateRequestDto) {
        var entity = modelCreateMapper.map(modelCreateRequestDto);
        var savedEntity = modelRepository.save(entity);
        return modelReadMapper.map(savedEntity);
    }

    @Transactional
    public ModelReadDto update(Long id, ModelUpdateDto modelUpdateDto) {
        var existingModel = getByIdOrElseThrow(id);

        var entity = modelUpdateMapper.map(modelUpdateDto, existingModel);
        var savedEntity = modelRepository.save(entity);
        return modelReadMapper.map(savedEntity);
    }


    public ModelReadDto getById(Long id) {
        return modelReadMapper.map(getByIdOrElseThrow(id));
    }


    public List<ModelReadDto> getAll() {
        return modelRepository.findAll().stream()
                .map(modelReadMapper::map)
                .collect(toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (modelRepository.existsById(id)) {
            modelRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<ModelReadDto> getAll(ModelFilterDto modelFilter, Integer page, Integer pageSize) {
        return modelRepository.findAll(modelPredicateBuilder.build(modelFilter), PageRequest.of(page, pageSize).withSort( Sort.Direction.ASC, "name")).map(modelReadMapper::map);
    }

    private Model getByIdOrElseThrow(Long id) {
        return modelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.prepare("Model", "id", id)));
    }
}