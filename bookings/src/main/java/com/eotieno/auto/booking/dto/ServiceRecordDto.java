package com.eotieno.auto.booking.dto;

import com.eotieno.auto.booking.dto.serviceProvider.ServiceProviderDetailsDTO;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRecordDto {

    private Long id;
    private String vehicleId;
    private LocalDate serviceDate; // maps preferredDate
    private String serviceType; // can be extended later (e.g. "repair", "maintenance")
    private String status; // "completed", "in-progress", "cancelled", "pending"

    private ServiceProviderDetailsDTO serviceProvider;
    private List<String> servicesPerformed;
    private String issueDescription;
    private String mechanicNotes;
    private Double totalCost;
    private String invoiceUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate preferredDate;
    private LocalTime preferredTime;
}
