package com.eotieno.auto.booking.dto.serviceProvider;


import com.eotieno.auto.booking.dto.mechanic.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbySearchRequest {
    private Double latitude;
    private Double longitude;
    private Double radius = 20.0; // Default 20km
    private ServiceCategory category;
    private String serviceName;
    private Boolean emergencyService;
    private Boolean mobileMechanic;
    private Integer minRating;
    private Boolean isVerified;
    private String businessType;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "distance";
    private String sortDirection = "ASC";
}