package com.eotieno.auto.user.exceptions;

import org.springframework.http.HttpStatus;

// For invalid requests (validation failures)
public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
