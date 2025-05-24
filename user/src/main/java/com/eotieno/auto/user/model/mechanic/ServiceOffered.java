package com.eotieno.auto.user.model.mechanic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOffered {
    private String id;
    private String name;
    private String description;
    private Double estimatedPrice;
    private Integer estimatedDuration; // in minutes
    private ServiceCategory category;
}