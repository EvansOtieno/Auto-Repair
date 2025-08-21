package com.eotieno.auto.booking.dto;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {}