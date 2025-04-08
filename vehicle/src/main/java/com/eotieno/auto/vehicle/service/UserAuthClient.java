package com.eotieno.auto.vehicle.service;

import com.eotieno.auto.vehicle.dto.AuthRequest;
import com.eotieno.auto.vehicle.dto.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-auth-client", url = "${user.service.url}")
public interface UserAuthClient {

    @PostMapping("/api/auth/login")  // Matches your User Service's login endpoint
    AuthResponse authenticate(@RequestBody AuthRequest request);
}
