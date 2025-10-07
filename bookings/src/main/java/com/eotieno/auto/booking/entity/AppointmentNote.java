package com.eotieno.auto.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// AppointmentNote entity
@Entity
@Table(name = "appointment_notes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false, length = 1000)
    private String note;

    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
