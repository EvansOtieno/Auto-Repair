package com.eotieno.auto.booking.entity;

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
    private Status status;

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

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
