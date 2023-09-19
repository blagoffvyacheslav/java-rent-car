package com.dmdev.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class ModelUpdateDto {

    @NotBlank(message = "Name is mandatory")
    String name;
}