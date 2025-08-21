package com.eotieno.auto.booking.exceptions;

import org.springframework.http.HttpStatus;

// For JWT-related errors
public class JwtTokenException extends BusinessException {
    public JwtTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

