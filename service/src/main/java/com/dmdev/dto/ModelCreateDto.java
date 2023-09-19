package com.dmdev.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class ModelCreateDto {

    @NotBlank(message = "Name is mandatory")
    String name;
}