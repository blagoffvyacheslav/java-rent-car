package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Set;

@Value
@Builder
public class CarReadDto {

    @NotEmpty
    Long id;
    String model;
    Integer yearOfProduction;
    String serialNumber;
    Boolean isNew;
    String image;
    Set<BigDecimal> prices;
}