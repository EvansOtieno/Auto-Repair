package com.eotieno.auto.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private String id;
    private String make;
    private String model;
    private int year;
    private String vin;
    private String licensePlate;
    private String color;
    private Long OwnerId;
    private LocationDTO location;
    private Map<String, Object> additionalDetails = new HashMap<>();
}