package com.eotieno.auto.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AppointmentCompletionDto {
    private Integer actualDuration;
    private BigDecimal totalAmount;
    private String completionNotes;
    private List<ServicePartDto> partsUsed;
    private Integer customerRating;

    // Service details
    private String servicePerformed;
    private String workQuality;
    private Boolean customerSatisfied;
    private Boolean followUpNeeded;
    private LocalDate followUpDate;
    private String followUpReason;

    // Billing details
    private BigDecimal laborCost;
    private BigDecimal partsCost;
    private BigDecimal additionalCharges;
    private String additionalChargesDescription;
    private BigDecimal discount;
    private String discountReason;
    private String paymentMethod;
}
