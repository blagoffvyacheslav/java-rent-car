package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class DamageFilter {
    LocalDate accidentDate;
    BigDecimal amount;
    String serialNumber;
    LocalDate orderDate;
}