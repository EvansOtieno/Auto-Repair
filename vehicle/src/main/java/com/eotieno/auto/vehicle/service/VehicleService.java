package com.eotieno.auto.vehicle.service;

import com.eotieno.auto.vehicle.config.JwtTokenUtil;
import com.eotieno.auto.vehicle.dto.UserDto;
import com.eotieno.auto.vehicle.dto.VehicleDto;
import com.eotieno.auto.vehicle.dto.VehicleRequest;
import com.eotieno.auto.vehicle.entity.ServiceRecord;
import com.eotieno.auto.vehicle.entity.Vehicle;
import com.eotieno.auto.vehicle.exceptions.ConflictException;
import com.eotieno.auto.vehicle.exceptions.ForbiddenException;
import com.eotieno.auto.vehicle.exceptions.NotFoundException;
import com.eotieno.auto.vehicle.repository.VehicleRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserServiceClient userServiceClient;
    private final JwtTokenUtil tokenUtil;

    public VehicleDto createVehicle(Vehicle vehicle) {
        // Validate owner exists via user service
        if (vehicle.getOwnerId() != null) {
            try {
                Vehicle savedVehicle = vehicleRepository.save(vehicle);
                return mapToVehicleDto(savedVehicle);
            } catch (Exception e) {
                throw new RuntimeException("Invalid owner ID or authentication failed");
            }
        } else {
            throw new RuntimeException("Invalid owner ID or authentication failed");
        }
    }

    public List<VehicleDto> getVehiclesByOwnerId(Long ownerId) {
        List<Vehicle> vehicles = vehicleRepository.findByOwnerId(ownerId);

        // Get owner details
        UserDto owner = null;
        try {
            owner = userServiceClient.getUserById(ownerId);
        } catch (Exception e) {
            // If we can't get owner details, just continue and return vehicles without owner info
        }

        final UserDto finalOwner = owner;
        return vehicles.stream()
                .map(this::mapToVehicleDto)
                .collect(Collectors.toList());
    }

    public Optional<VehicleDto> getVehicleById(String id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    UserDto owner = null;
                    if (vehicle.getOwnerId() != null) {
                        try {
                            owner = userServiceClient.getUserById(vehicle.getOwnerId());
                        } catch (Exception e) {
                            // Continue without owner info
                        }
                    }
                    return mapToVehicleDto(vehicle);
                });
    }

    public Optional<VehicleDto> getVehicleByVin(String vin) {
        return vehicleRepository.findByVin(vin)
                .map(vehicle -> {
                    UserDto owner = null;
                    if (vehicle.getOwnerId() != null) {
                        try {
                            owner = userServiceClient.getUserById(vehicle.getOwnerId());
                        } catch (Exception e) {
                            // Continue without owner info
                        }
                    }
                    return mapToVehicleDto(vehicle);
                });
    }

    public Optional<Vehicle> updateVehicle(String id, Vehicle updatedVehicle) {
        return vehicleRepository.findById(id)
                .map(existingVehicle -> {
                    // Update basic info
                    existingVehicle.setMake(updatedVehicle.getMake());
                    existingVehicle.setModel(updatedVehicle.getModel());
                    existingVehicle.setYear(updatedVehicle.getYear());
                    existingVehicle.setColor(updatedVehicle.getColor());
                    existingVehicle.setLicensePlate(updatedVehicle.getLicensePlate());

                    // Update additional details
                    if (updatedVehicle.getAdditionalDetails() != null) {
                        existingVehicle.setAdditionalDetails(updatedVehicle.getAdditionalDetails());
                    }

                    return vehicleRepository.save(existingVehicle);
                });
    }

    public Optional<Vehicle> addServiceRecord(String vehicleId, ServiceRecord serviceRecord) {
        // Generate ID for service record if not provided
        if (serviceRecord.getId() == null) {
            serviceRecord.setId(UUID.randomUUID().toString());
        }

        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> {
                    vehicle.getServiceHistory().add(serviceRecord);
                    return vehicleRepository.save(vehicle);
                });
    }

    public void deleteVehicle(String id) {
        vehicleRepository.deleteById(id);
    }

    private VehicleDto mapToVehicleDto(Vehicle vehicle) {
        VehicleDto dto = new VehicleDto();
        dto.setId(vehicle.getId());
        dto.setMake(vehicle.getMake());
        dto.setModel(vehicle.getModel());
        dto.setYear(vehicle.getYear());
        dto.setVin(vehicle.getVin());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setColor(vehicle.getColor());
        dto.setOwnerId(vehicle.getOwnerId());
        dto.setAdditionalDetails(vehicle.getAdditionalDetails());
        return dto;
    }
}