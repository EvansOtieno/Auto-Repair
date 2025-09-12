package com.eotieno.auto.booking.dto.serviceProvider;


import com.eotieno.auto.booking.dto.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDTO {
    private String id;
    private String name;
    private LocationDTO location;
    private List<String> services;
    private Double rating;
    private String phoneNumber;
    private String email;
    private String workingHours;
    private String description;
}


