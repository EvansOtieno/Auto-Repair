package com.eotieno.auto.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AppointmentStatsDto {
    private Long totalAppointments;
    private Long pendingCount;
    private Long confirmedCount;
    private Long inProgressCount;
    private Long completedCount;
    private Long cancelledCount;
    private Long noShowCount;
    private Long todayAppointments;
    private BigDecimal thisWeekRevenue;
    private Double averageRating;
}
