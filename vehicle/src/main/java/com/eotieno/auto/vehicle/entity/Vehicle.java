package com.eotieno.auto.vehicle.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vehicles")
public class Vehicle {
    @Id
    private String id;

    private String make;
    private String model;
    private int year;
    private String vin;
    private String licensePlate;
    private String color;

    // Service history
    private List<ServiceRecord> serviceHistory = new ArrayList<>();

    // Flexible data storage for additional vehicle details
    private Map<String, Object> additionalDetails = new HashMap<>();

    private Long ownerId;
}