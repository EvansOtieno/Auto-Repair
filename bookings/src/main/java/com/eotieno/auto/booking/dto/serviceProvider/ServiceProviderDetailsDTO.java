package com.eotieno.auto.booking.dto.serviceProvider;



import com.eotieno.auto.booking.dto.LocationDTO;
import com.eotieno.auto.booking.dto.mechanic.BusinessType;
import com.eotieno.auto.booking.dto.mechanic.CertificationDTO;
import com.eotieno.auto.booking.dto.mechanic.ServiceOfferedDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDetailsDTO {
    // Basic ServiceProvider fields
    private String id;
    private String name;
    private LocationDTO location;
    private List<String> services;
    private Double rating;
    private String phoneNumber;
    private String email;
    private String workingHours;
    private String description;

    // Additional details
    private BusinessType businessType;
    private List<String> specializations;
    private Integer yearsOfExperience;
    private List<ServiceOfferedDTO> servicesOffered;
    private List<CertificationDTO> certifications;
    private Boolean emergencyService;
    private Boolean mobileMechanic;
    private String website;
    private String profileImageUrl;
    private Boolean isVerified;
    private Integer totalReviews;
}
