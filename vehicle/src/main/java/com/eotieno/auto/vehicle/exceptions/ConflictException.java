package com.eotieno.auto.vehicle.exceptions;

import org.springframework.http.HttpStatus;

// When a unique constraint is violated (like duplicate VIN)
public class ConflictException extends BusinessException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
