package com.eotieno.auto.booking.dto.mechanic;


import com.eotieno.auto.booking.dto.LocationDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MechanicProfileDTO {
    private String id;
    @NotBlank(message = "User ID is required")
    private String userId;
    @NotBlank(message = "Business name is required")
    @Size(min = 2, message = "Business name must be at least 2 characters")
    private String businessName;
    @NotNull(message = "Business type is required")
    private BusinessType businessType;
    @NotBlank(message = "Description is required")
    @Size(min = 20, message = "Description must be at least 20 characters")
    private String description;
    private List<String> specializations;
    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;
    @NotNull(message = "Location is required")
    private LocationDTO location;
    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^\\+?[\\d\\s\\-\\(\\)]+$", message = "Invalid phone number format")
    private String contactPhone;
    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;
    private String website;
    private List<ServiceOfferedDTO> servicesOffered;
    private List<WorkingHoursDTO> workingHours;
    private List<CertificationDTO> certifications;
    private String licenseNumber;
    private String insuranceProvider;
    private Boolean emergencyService;
    private Boolean mobileMechanic;
    private Double averageRating;
    private Integer totalReviews;
    private Boolean isProfileComplete;
    private Boolean isVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}