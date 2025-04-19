package com.eotieno.auto.user.dto;

public record ErrorResponse(
        int status,
        String message,
        long timestamp
) {}