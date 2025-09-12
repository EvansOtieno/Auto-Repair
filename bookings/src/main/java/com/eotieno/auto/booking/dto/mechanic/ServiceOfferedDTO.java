package com.eotieno.auto.booking.dto.mechanic;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOfferedDTO {
    private String id;
    @NotBlank(message = "Service name is required")
    private String name;
    @NotBlank(message = "Service description is required")
    private String description;
    @NotNull(message = "Estimated price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private Double estimatedPrice;
    @NotNull(message = "Estimated duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    private Integer estimatedDuration;
    @NotNull(message = "Service category is required")
    private ServiceCategory category;
}