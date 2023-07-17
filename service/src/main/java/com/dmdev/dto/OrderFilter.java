package com.dmdev.dto;

import com.dmdev.entity.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class OrderFilter {
    LocalDate date;
    String userEmail;
    String userFirstName;
    String userLastName;
    String carNumber;
    String modelName;
    OrderStatus orderStatus;
    BigDecimal sum;
    boolean insurance;
}