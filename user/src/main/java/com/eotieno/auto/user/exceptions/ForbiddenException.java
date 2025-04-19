package com.eotieno.auto.user.exceptions;

import org.springframework.http.HttpStatus;

// When user lacks proper permissions
public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
