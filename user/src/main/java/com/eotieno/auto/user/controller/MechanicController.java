package com.eotieno.auto.user.controller;

import com.eotieno.auto.user.model.mechanic.BusinessType;
import com.eotieno.auto.user.model.mechanic.MechanicProfile;
import com.eotieno.auto.user.model.mechanic.ServiceCategory;
import com.eotieno.auto.user.service.MechanicProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mechanic-profiles")
public class MechanicController {

    @Autowired
    private MechanicProfileService mechanicProfileService;

    // Create a new mechanic profile
    @PostMapping
    public ResponseEntity<MechanicProfile> createProfile(@Valid @RequestBody MechanicProfile profile) {
        try {
            MechanicProfile savedProfile = mechanicProfileService.createProfile(profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get all mechanic profiles
    @GetMapping
    public ResponseEntity<List<MechanicProfile>> getAllProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "businessName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        List<MechanicProfile> profiles = mechanicProfileService.findAll();
        return ResponseEntity.ok(profiles);
    }

    // Get mechanic profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<MechanicProfile> getProfileById(@PathVariable String id) {
        Optional<MechanicProfile> profile = mechanicProfileService.findByIdOrUserId(id,id);
        return profile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get mechanic profile by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<MechanicProfile> getProfileByUserId(@PathVariable String userId) {
        Optional<MechanicProfile> profile = mechanicProfileService.findByUserId(userId);
        return profile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update mechanic profile
    @PutMapping("/{id}")
    public ResponseEntity<MechanicProfile> updateProfile(
            @PathVariable String id,
            @Valid @RequestBody MechanicProfile updatedProfile) {
        try {
            MechanicProfile profile = mechanicProfileService.updateProfile(id, updatedProfile);
            if (profile != null) {
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete mechanic profile
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String id) {
        try {
            Optional<MechanicProfile> existingProfile = mechanicProfileService.findById(id);
            if (existingProfile.isPresent()) {
                mechanicProfileService.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search profiles by business name
    @GetMapping("/search")
    public ResponseEntity<List<MechanicProfile>> searchByBusinessName(
            @RequestParam String businessName) {
        List<MechanicProfile> profiles = mechanicProfileService.searchByBusinessName(businessName);
        return ResponseEntity.ok(profiles);
    }

    // Get verified mechanics
    @GetMapping("/verified")
    public ResponseEntity<List<MechanicProfile>> getVerifiedMechanics() {
        List<MechanicProfile> profiles = mechanicProfileService.findVerifiedMechanics();
        return ResponseEntity.ok(profiles);
    }

    // Get profiles by business type
    @GetMapping("/business-type/{businessType}")
    public ResponseEntity<List<MechanicProfile>> getProfilesByBusinessType(
            @PathVariable BusinessType businessType) {
        List<MechanicProfile> profiles = mechanicProfileService.findByBusinessType(businessType);
        return ResponseEntity.ok(profiles);
    }

    // Get profiles by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<MechanicProfile>> getProfilesByCity(@PathVariable String city) {
        List<MechanicProfile> profiles = mechanicProfileService.findByCity(city);
        return ResponseEntity.ok(profiles);
    }

    // Get profiles by specialization
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<MechanicProfile>> getProfilesBySpecialization(
            @PathVariable String specialization) {
        List<MechanicProfile> profiles = mechanicProfileService.findBySpecialization(specialization);
        return ResponseEntity.ok(profiles);
    }

    // Get profiles by service category
    @GetMapping("/service-category/{category}")
    public ResponseEntity<List<MechanicProfile>> getProfilesByServiceCategory(
            @PathVariable ServiceCategory category) {
        List<MechanicProfile> profiles = mechanicProfileService.findByServiceCategory(category);
        return ResponseEntity.ok(profiles);
    }

    // Get emergency service mechanics
    @GetMapping("/emergency")
    public ResponseEntity<List<MechanicProfile>> getEmergencyMechanics() {
        List<MechanicProfile> profiles = mechanicProfileService.findEmergencyMechanics();
        return ResponseEntity.ok(profiles);
    }

    // Get mobile mechanics
    @GetMapping("/mobile")
    public ResponseEntity<List<MechanicProfile>> getMobileMechanics() {
        List<MechanicProfile> profiles = mechanicProfileService.findMobileMechanics();
        return ResponseEntity.ok(profiles);
    }

    // Get top rated mechanics
    @GetMapping("/top-rated")
    public ResponseEntity<List<MechanicProfile>> getTopRatedMechanics() {
        List<MechanicProfile> profiles = mechanicProfileService.findTopRatedMechanics();
        return ResponseEntity.ok(profiles);
    }

    // Get mechanics near location
    @GetMapping("/near")
    public ResponseEntity<List<MechanicProfile>> getMechanicsNearLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double radiusKm) {
        List<MechanicProfile> profiles = mechanicProfileService.findNearLocation(latitude, longitude, radiusKm);
        return ResponseEntity.ok(profiles);
    }

    // Verify a mechanic profile
    @PutMapping("/{id}/verify")
    public ResponseEntity<MechanicProfile> verifyProfile(@PathVariable String id) {
        MechanicProfile profile = mechanicProfileService.verifyProfile(id);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update mechanic rating
    @PutMapping("/{id}/rating")
    public ResponseEntity<MechanicProfile> updateRating(
            @PathVariable String id,
            @RequestParam Double rating,
            @RequestParam Integer totalReviews) {
        MechanicProfile profile = mechanicProfileService.updateRating(id, rating, totalReviews);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get statistics - count verified mechanics by city
    @GetMapping("/stats/verified-count/{city}")
    public ResponseEntity<Long> getVerifiedMechanicsCountByCity(@PathVariable String city) {
        Long count = mechanicProfileService.countVerifiedMechanicsByCity(city);
        return ResponseEntity.ok(count);
    }

    // Get recently updated profiles
    @GetMapping("/recent")
    public ResponseEntity<List<MechanicProfile>> getRecentlyUpdatedProfiles(
            @RequestParam(defaultValue = "24") int hours) {
        List<MechanicProfile> profiles = mechanicProfileService.findRecentlyUpdatedProfiles(hours);
        return ResponseEntity.ok(profiles);
    }

    // Advanced search with pagination
    @GetMapping("/search/advanced")
    public ResponseEntity<Page<MechanicProfile>> advancedSearch(
            @RequestParam(required = false) String businessName,
            @RequestParam(required = false) Boolean isVerified,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "businessName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MechanicProfile> profiles = mechanicProfileService.searchMechanics(
                businessName != null ? businessName : ".*",
                isVerified,
                pageable
        );
        return ResponseEntity.ok(profiles);
    }
}