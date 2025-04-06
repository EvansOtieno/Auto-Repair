package com.eotieno.auto.vehicle.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleResponse {
    private Long id;
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private Long ownerId;
}
