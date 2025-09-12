package com.eotieno.auto.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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

    public LocationDTO(double lat, double lng, String address) {
        this.latitude = lat;
        this.longitude = lng;
        this.address = address;
    }
}