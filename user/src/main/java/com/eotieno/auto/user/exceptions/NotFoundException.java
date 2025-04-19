package com.eotieno.auto.user.exceptions;

import org.springframework.http.HttpStatus;

// When a resource isn't found
public class NotFoundException extends BusinessException {
    public NotFoundException(String resourceName, String id) {
        super(resourceName + " not found with ID: " + id, HttpStatus.NOT_FOUND);
    }
}
