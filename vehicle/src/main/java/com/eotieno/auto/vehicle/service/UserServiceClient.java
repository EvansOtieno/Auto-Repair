package com.eotieno.auto.vehicle.service;

import com.eotieno.auto.vehicle.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {

    // Check if user exists
    @GetMapping("/{userId}/exists")
    boolean userExists(@PathVariable Long userId);

    // Get user role (assuming your UserResponse contains roles)
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable Long userId);

    // OR if you have a dedicated role endpoint
    @GetMapping("/{userId}/role")
    Set<String> getUserRoles(@PathVariable Long userId);
}
