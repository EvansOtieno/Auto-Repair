package com.eotieno.auto.vehicle.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles", indexes = {
        @Index(name = "idx_vin", columnList = "vin", unique = true),
        @Index(name = "idx_owner", columnList = "ownerId")
})
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String vin;  // Vehicle Identification Number

    private String make;
    private String model;
    private Integer year;
    private String licensePlate;

    private Long ownerId;  // References User Service's ID (not a FK)
}