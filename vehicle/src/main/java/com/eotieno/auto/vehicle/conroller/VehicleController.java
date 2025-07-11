package com.eotieno.auto.vehicle.conroller;

import com.eotieno.auto.vehicle.dto.VehicleDto;
import com.eotieno.auto.vehicle.entity.ServiceRecord;
import com.eotieno.auto.vehicle.entity.Vehicle;
import com.eotieno.auto.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/register")
    public ResponseEntity<VehicleDto> createVehicle(
            @RequestBody Vehicle vehicle,
            @RequestHeader("Authorization") String authToken) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.createVehicle(vehicle));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<VehicleDto>> getVehiclesByOwner(
            @PathVariable Long ownerId,
            @RequestHeader("Authorization") String authToken) {
        return ResponseEntity.ok(vehicleService.getVehiclesByOwnerId(ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(
            @PathVariable String id,
            @RequestHeader("Authorization") String authToken) {
        return vehicleService.getVehicleById(id).or(() -> vehicleService.getVehicleByVin(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vin/{vin}")
    public ResponseEntity<VehicleDto> getVehicleByVin(
            @PathVariable String vin,
            @RequestHeader("Authorization") String authToken) {
        return vehicleService.getVehicleByVin(vin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable String id,
            @RequestBody Vehicle vehicle,
            @RequestHeader("Authorization") String authToken) {
        return vehicleService.updateVehicle(id, vehicle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/service")
    public ResponseEntity<Vehicle> addServiceRecord(
            @PathVariable String id,
            @RequestBody ServiceRecord serviceRecord,
            @RequestHeader("Authorization") String authToken) {
        return vehicleService.addServiceRecord(id, serviceRecord)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable String id,
            @RequestHeader("Authorization") String authToken) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}

