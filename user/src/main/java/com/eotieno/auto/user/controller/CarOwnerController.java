package com.eotieno.auto.user.controller;

import com.eotieno.auto.user.model.car_owner.CarOwnerProfile;
import com.eotieno.auto.user.model.mechanic.MechanicProfile;
import com.eotieno.auto.user.service.CarOwnerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car-owner")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CAR_OWNER')")
public class CarOwnerController {
    private final CarOwnerProfileService profileService;
    @GetMapping("/dashboard")
    public ResponseEntity<String> carOwnerDashboard() {
        return ResponseEntity.ok("Welcome, Car Owner!");
    }

//    @GetMapping("/my-vehicles")
//    public ResponseEntity<List<Vehicle>> getMyVehicles() {
//        // Fetch vehicles linked to the authenticated user
//        return ResponseEntity.ok(vehicleService.getVehiclesForCurrentUser());
//    }

    @PostMapping
    public ResponseEntity<CarOwnerProfile> createProfile(
            @RequestBody CarOwnerProfile profile) {

        try {
            CarOwnerProfile createdProfile = profileService.createProfile(profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarOwnerProfile> getProfile(@PathVariable String id) {
        return ResponseEntity.ok(profileService.getProfileByUserId(Long.valueOf(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CarOwnerProfile> getProfileByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarOwnerProfile> updateProfile(
            @PathVariable String id,
            @RequestBody CarOwnerProfile updatedProfile) {
        return ResponseEntity.ok(profileService.updateProfile(id, updatedProfile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String id) {
        profileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
