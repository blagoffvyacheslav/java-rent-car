package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CarFilterDto {

    Boolean isNew;
    String serialNumber;
    List<String> modelNames;
}