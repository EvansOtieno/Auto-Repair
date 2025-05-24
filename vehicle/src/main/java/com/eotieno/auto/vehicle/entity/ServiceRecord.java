package com.eotieno.auto.vehicle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecord {
    private String id;
    private LocalDate serviceDate;
    private String serviceType; // e.g., "oil change", "brake replacement"
    private String serviceFacility;
    private String technicianName;
    private double cost;
    private String description;
    private int mileage;

    // Flexible data for additional service details
    private Map<String, Object> additionalDetails = new HashMap<>();
}