package com.eotieno.auto.vehicle.service;

import com.eotieno.auto.vehicle.dto.VehicleRequest;
import com.eotieno.auto.vehicle.entity.Vehicle;
import com.eotieno.auto.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserServiceClient userServiceClient;

    public Vehicle registerVehicle(VehicleRequest request, Long ownerId) {
        // Validate owner exists and is a CAR_OWNER
        if (!userServiceClient.userExists(ownerId)) {
            throw new IllegalArgumentException("User not found");
        }

        // Add duplicate check
        if (vehicleRepository.existsByVin(request.getVin())) {
            throw new IllegalArgumentException("VIN already registered");
        }

        Set<String> roles = userServiceClient.getUserRoles(ownerId);
        if (roles == null || !roles.contains("ROLE_CAR_OWNER")) {
            throw new IllegalArgumentException(
                    "User must have ROLE_CAR_OWNER. Actual roles: " + roles
            );
        }

        Vehicle vehicle = Vehicle.builder()
                .vin(request.getVin())
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .ownerId(ownerId)
                .build();

        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getVehiclesByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }
}