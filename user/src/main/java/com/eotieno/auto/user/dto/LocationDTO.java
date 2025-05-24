package com.eotieno.auto.user.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    @NotNull(message = "Latitude is required")
    private Double latitude;
    @NotNull(message = "Longitude is required")
    private Double longitude;
    @NotNull(message = "Address is required")
    private String address;
    @NotNull(message = "City is required")
    private String city;
    @NotNull(message = "State is required")
    private String state;
    private String zipCode;
    @NotNull(message = "Country is required")
    private String country;
}