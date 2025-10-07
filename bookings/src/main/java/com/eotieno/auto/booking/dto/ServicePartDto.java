package com.eotieno.auto.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ServicePartDto {
    private Long id;
    private String partName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String supplier;
    private String partNumber;
}
