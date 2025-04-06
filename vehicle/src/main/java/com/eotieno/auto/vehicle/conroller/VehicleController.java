package com.eotieno.auto.vehicle.conroller;

import com.eotieno.auto.vehicle.dto.VehicleRequest;
import com.eotieno.auto.vehicle.dto.VehicleResponse;
import com.eotieno.auto.vehicle.entity.Vehicle;
import com.eotieno.auto.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse registerVehicle(
            @RequestBody VehicleRequest request,
            @RequestHeader("Authorization") String token,
            @RequestParam Long ownerId) {
        Vehicle vehicle = vehicleService.registerVehicle(request, ownerId);
        return mapToResponse(vehicle);
    }

    @GetMapping("/owner/{ownerId}")
    public List<VehicleResponse> getVehiclesByOwner(@PathVariable Long ownerId) {
        return vehicleService.getVehiclesByOwner(ownerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .vin(vehicle.getVin())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .licensePlate(vehicle.getLicensePlate())
                .ownerId(vehicle.getOwnerId())
                .build();
    }
}