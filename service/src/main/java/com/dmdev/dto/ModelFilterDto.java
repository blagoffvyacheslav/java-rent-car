package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ModelFilterDto {

    List<String> models;
}