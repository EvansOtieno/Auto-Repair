package com.eotieno.auto.user.model.mechanic;

import com.eotieno.auto.user.model.Location;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;


import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mechanic_profiles")
public class MechanicProfile {
    @Id
    private String id;

    @NotBlank(message = "User ID is required")
    @Indexed(unique = true)
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

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private Location location;

    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^\\+?[\\d\\s\\-\\(\\)]+$", message = "Invalid phone number format")
    private String contactPhone;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    private String website;

    private List<ServiceOffered> servicesOffered;
    private List<WorkingHours> workingHours;
    private List<Certification> certifications;

    private String licenseNumber;
    private String insuranceProvider;

    @NotNull
    private Boolean emergencyService = false;

    @NotNull
    private Boolean mobileMechanic = false;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Double averageRating;

    @Min(value = 0, message = "Total reviews cannot be negative")
    private Integer totalReviews;

    @NotNull
    private Boolean isProfileComplete = false;

    @NotNull
    private Boolean isVerified = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public double[] getLocationCoordinates() {
        if (location != null && location.getLongitude() != null && location.getLatitude() != null) {
            return new double[]{location.getLongitude(), location.getLatitude()};
        }
        return null;
    }
}