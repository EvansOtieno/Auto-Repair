package com.eotieno.auto.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AppointmentEstimateDto {
    private Integer estimatedDuration;
    private BigDecimal estimatedCost;
}
