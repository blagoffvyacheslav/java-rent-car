package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CarFilter {
    String modelName;
    String serialNumber;
    String price;
    boolean isNew;

}