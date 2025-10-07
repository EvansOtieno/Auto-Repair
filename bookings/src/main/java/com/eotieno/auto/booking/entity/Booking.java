package com.eotieno.auto.booking.entity;

import com.eotieno.auto.booking.dto.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private AppointmentStatus status;

    // Extended fields for appointment management
    private Integer estimatedDuration; // in minutes
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private String completionNotes;
    private Integer customerRating;
    private BigDecimal totalAmount;

    // Service tracking
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

    // Rescheduling
    private String rescheduleReason;
    private String cancelReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppointmentNote> notes;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServicePart> partsUsed;

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