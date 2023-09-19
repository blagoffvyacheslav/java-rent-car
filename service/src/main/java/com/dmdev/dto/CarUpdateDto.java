package com.dmdev.dto;

import lombok.Value;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class CarUpdateDto {

    @NotNull
    Long modelId;

    @NotBlank(message = "Car serialNumber is mandatory")
    String serialNumber;

    @NotNull
    Boolean isNew;

    MultipartFile image;
}