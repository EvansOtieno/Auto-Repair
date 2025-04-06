package com.eotieno.auto.vehicle.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {
    @NotBlank
    private String vin;
    @NotBlank private String make;
    @NotBlank private String model;
    @NotNull
    @Min(1900) private Integer year;
    private String licensePlate;
}
