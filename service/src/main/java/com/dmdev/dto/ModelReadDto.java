package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
public class ModelReadDto {

    @NotEmpty
    Long id;

    String name;
}