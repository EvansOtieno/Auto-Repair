package com.eotieno.auto.user.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/car-owner")
@RequiredArgsConstructor
public class CarOwnerController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> carOwnerDashboard() {
        return ResponseEntity.ok("Welcome, Car Owner!");
    }

//    @GetMapping("/my-vehicles")
//    public ResponseEntity<List<Vehicle>> getMyVehicles() {
//        // Fetch vehicles linked to the authenticated user
//        return ResponseEntity.ok(vehicleService.getVehiclesForCurrentUser());
//    }
}
