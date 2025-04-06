package com.eotieno.auto.vehicle.repository;

import com.eotieno.auto.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Find by VIN (unique)
    Optional<Vehicle> findByVin(String vin);

    // Find all vehicles owned by a specific user
    List<Vehicle> findByOwnerId(Long ownerId);

    // Check if license plate exists (optional)
    boolean existsByLicensePlate(String licensePlate);

    // Check if VIN exists (optional)
    boolean existsByVin(String vin);
}