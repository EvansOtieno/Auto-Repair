package com.eotieno.auto.booking.service;


import com.eotieno.auto.booking.dto.AuthRequest;
import com.eotieno.auto.booking.dto.AuthResponse;
import com.eotieno.auto.booking.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserServiceClient {

    @PostMapping("/api/auth/login")
    AuthResponse authenticate(@RequestBody AuthRequest request);

    // Check if user exists
    @GetMapping("/api/users/{userId}/exists")
    boolean userExists(@PathVariable Long userId);

    // OR if you have a dedicated endpoint for username
    @GetMapping("/api/users/username/{userName}")
    UserDto getUserByIdentity(@PathVariable("userIdentity") String userId);

    // OR if you have a dedicated role endpoint
    @GetMapping("/api/users/{userId}/roles")
    Set<String> getUserRoles(@PathVariable Long userId);

    @GetMapping("/api/users/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);


//    @FeignClient(name = "user-service", configuration = FeignConfig.class)
//    public interface UserServiceClient {
//        @GetMapping("/api/users/{id}")
//        User getUser(@PathVariable Long id);
//    }

}
