package com.eotieno.auto.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AppointmentDto {
    private Long id;
    private String vehicleId;
    private String customerId;
    private String serviceProviderId;
    private LocalDateTime preferredDate;
    private String preferredTime;
    private String issueDescription;
    private CustomerDetailsDto customerDetails;
    private AppointmentStatus status;
    private Integer estimatedDuration;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private String completionNotes;
    private Integer customerRating;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AppointmentNoteDto> serviceNotes;
}
