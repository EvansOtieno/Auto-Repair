package com.eotieno.auto.vehicle.service;

import com.eotieno.auto.vehicle.dto.VehicleRequest;
import com.eotieno.auto.vehicle.entity.Vehicle;
import com.eotieno.auto.vehicle.exceptions.ConflictException;
import com.eotieno.auto.vehicle.exceptions.ForbiddenException;
import com.eotieno.auto.vehicle.exceptions.NotFoundException;
import com.eotieno.auto.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VehicleService {
    @Autowired
    private final VehicleRepository vehicleRepository;
    @Autowired
    private final UserServiceClient userServiceClient;

    public List<Vehicle> getVehiclesByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }

    public Vehicle registerVehicle(VehicleRequest request, Long ownerId, Long authenticatedUserId) {
        // 1. Verify authenticated user is either the owner or an admin
        validateOwnership(authenticatedUserId, ownerId);

        // 2. Check if owner exists
        validateUserExists(ownerId);

        // 3. Ensure VIN is unique
        validateVinUniqueness(request.getVin());

        // 4. Verify owner has required role
        validateUserRole(ownerId);

        // 5. Create and save vehicle
        return vehicleRepository.save(buildVehicle(request, ownerId));
    }

    // Helper Methods
    private void validateOwnership(Long authenticatedUserId, Long ownerId) {
        if (!authenticatedUserId.equals(ownerId)) {
            Set<String> roles = userServiceClient.getUserRoles(authenticatedUserId);
            if (!roles.contains("ROLE_ADMIN")) {
                throw new ForbiddenException("User cannot register vehicles for others");
            }
        }
    }

    private void validateUserExists(Long ownerId) {
        if (!userServiceClient.userExists(ownerId)) {
            throw new NotFoundException("User"  ,ownerId);
        }
    }

    private void validateVinUniqueness(String vin) {
        if (vehicleRepository.existsByVin(vin)) {
            throw new ConflictException("VIN already registered: " + vin);
        }
    }

    private void validateUserRole(Long ownerId) {
        Set<String> roles = userServiceClient.getUserRoles(ownerId);
        if (!roles.contains("ROLE_CAR_OWNER")) {
            throw new ForbiddenException("User lacks ROLE_CAR_OWNER");
        }
    }

    private Vehicle buildVehicle(VehicleRequest request, Long ownerId) {
        return Vehicle.builder()
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .ownerId(ownerId)
                .build();
    }
}