package com.eotieno.auto.user.controller;

import com.eotieno.auto.user.dto.serviceProvider.NearbySearchRequest;
import com.eotieno.auto.user.dto.serviceProvider.ServiceProviderDTO;
import com.eotieno.auto.user.dto.serviceProvider.ServiceProviderDetailsDTO;
import com.eotieno.auto.user.model.mechanic.ServiceCategory;
import com.eotieno.auto.user.service.ServiceProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-providers")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class ServiceProviderController {

    @Autowired
    private ServiceProviderService serviceProviderService;

    /**
     * GET NEARBY SERVICE PROVIDERS - Main endpoint
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<ServiceProviderDTO>> getNearbyServiceProviders(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "20") Double radius,
            @RequestParam(required = false) ServiceCategory category,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) Boolean emergencyService,
            @RequestParam(required = false) Boolean mobileMechanic,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Boolean isVerified,
            @RequestParam(required = false) String businessType,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "distance") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        try {
            log.info("Searching for nearby service providers at lat: {}, lng: {}, radius: {}km",
                    lat, lng, radius);

            NearbySearchRequest request = NearbySearchRequest.builder()
                    .latitude(lat)
                    .longitude(lng)
                    .radius(radius)
                    .category(category)
                    .serviceName(serviceName)
                    .emergencyService(emergencyService)
                    .mobileMechanic(mobileMechanic)
                    .minRating(minRating)
                    .isVerified(isVerified)
                    .businessType(businessType)
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();

            List<ServiceProviderDTO> providers = serviceProviderService.getNearbyServiceProviders(request);

            log.info("Found {} nearby service providers", providers.size());
            return ResponseEntity.ok(providers);

        } catch (Exception e) {
            log.error("Error finding nearby service providers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET SERVICE PROVIDER DETAILS BY ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderDetailsDTO> getServiceProviderDetails(@PathVariable String id) {
        try {
            log.info("Fetching service provider details for ID: {}", id);

            ServiceProviderDetailsDTO details = serviceProviderService.getServiceProviderDetails(id);

            if (details == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(details);

        } catch (Exception e) {
            log.error("Error retrieving service provider details for ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET NEARBY SERVICE PROVIDERS BY CATEGORY
     */
    @GetMapping("/nearby/category/{category}")
    public ResponseEntity<List<ServiceProviderDTO>> getNearbyServiceProvidersByCategory(
            @PathVariable("category") String categoryStr,
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "20") Double radius) {

        try {
            ServiceCategory category = ServiceCategory.valueOf(categoryStr.toUpperCase());

            List<ServiceProviderDTO> providers = serviceProviderService
                    .getNearbyServiceProvidersByCategory(lat, lng, radius, category);

            return ResponseEntity.ok(providers);

        } catch (IllegalArgumentException e) {
            log.error("Invalid service category: {}", categoryStr);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error finding service providers by category: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET NEARBY EMERGENCY SERVICES
     */
    @GetMapping("/nearby/emergency")
    public ResponseEntity<List<ServiceProviderDTO>> getNearbyEmergencyServices(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "50") Double radius) {

        try {
            List<ServiceProviderDTO> providers = serviceProviderService
                    .getNearbyEmergencyServices(lat, lng, radius);

            return ResponseEntity.ok(providers);

        } catch (Exception e) {
            log.error("Error finding emergency services: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET NEARBY MOBILE SERVICES
     */
    @GetMapping("/nearby/mobile")
    public ResponseEntity<List<ServiceProviderDTO>> getNearbyMobileServices(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "30") Double radius) {

        try {
            List<ServiceProviderDTO> providers = serviceProviderService
                    .getNearbyMobileServices(lat, lng, radius);

            return ResponseEntity.ok(providers);

        } catch (Exception e) {
            log.error("Error finding mobile services: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * SEARCH SERVICE PROVIDERS BY SERVICE NAME
     */
    @GetMapping("/search")
    public ResponseEntity<List<ServiceProviderDTO>> searchServiceProviders(
            @RequestParam String serviceName,
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "25") Double radius) {

        try {
            if (serviceName == null || serviceName.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<ServiceProviderDTO> providers = serviceProviderService
                    .searchServiceProvidersByService(lat, lng, serviceName.trim(), radius);

            return ResponseEntity.ok(providers);

        } catch (Exception e) {
            log.error("Error searching service providers by service name: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET TOP-RATED NEARBY SERVICES
     */
    @GetMapping("/nearby/top-rated")
    public ResponseEntity<List<ServiceProviderDTO>> getTopRatedNearbyServices(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "20") Double radius,
            @RequestParam(defaultValue = "4") Integer minRating) {

        try {
            List<ServiceProviderDTO> providers = serviceProviderService
                    .getTopRatedNearbyServices(lat, lng, radius, minRating);

            return ResponseEntity.ok(providers);

        } catch (Exception e) {
            log.error("Error finding top-rated services: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * CALCULATE DISTANCE BETWEEN TWO POINTS
     */
    @GetMapping("/distance")
    public ResponseEntity<Double> calculateDistance(
            @RequestParam Double lat1,
            @RequestParam Double lng1,
            @RequestParam Double lat2,
            @RequestParam Double lng2) {

        try {
            Double distance = serviceProviderService.calculateDistance(lat1, lng1, lat2, lng2);
            return ResponseEntity.ok(distance);

        } catch (Exception e) {
            log.error("Error calculating distance: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}