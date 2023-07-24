package com.dmdev.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class OrderDetailsFilter {
    String orderId;
    LocalDateTime startDate;
    LocalDateTime endDate;

}