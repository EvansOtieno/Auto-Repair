package com.eotieno.auto.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleId;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String serviceProviderId;

    @Column(nullable = false)
    private LocalDate preferredDate;

    @Column(nullable = false)
    private LocalTime preferredTime;

    @Column(length = 1000)
    private String issueDescription;

    @Embedded
    private CustomerDetails customerDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    @Embeddable
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

