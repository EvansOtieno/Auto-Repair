package com.eotieno.auto.booking.entity;

import com.eotieno.auto.booking.dto.AppointmentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    private String vehicleId;
    private String customerId;
    private String serviceProviderId;
    private LocalDate preferredDate;
    private LocalTime preferredTime;
    private String issueDescription;
    private CustomerDetails customerDetails;
    private AppointmentStatus status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerDetails {
        private Long userId;
        private String name;
        private String phone;
        private String email;
    }
}
