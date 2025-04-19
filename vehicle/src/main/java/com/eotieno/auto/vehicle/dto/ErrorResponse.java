package com.eotieno.auto.vehicle.dto;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {}