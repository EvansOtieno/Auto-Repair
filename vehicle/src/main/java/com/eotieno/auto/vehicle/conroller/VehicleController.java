package com.eotieno.auto.vehicle.conroller;

import com.eotieno.auto.vehicle.config.JwtTokenUtil;
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
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponse registerVehicle(
            @RequestBody VehicleRequest request,
            @RequestHeader(value = "Authorization") String token,
            @RequestParam Long ownerId) {
        // Extract user ID from JWT
        Long authenticatedUserId = jwtTokenUtil.getUserIdFromToken(token.substring(7));

        Vehicle vehicle = vehicleService.registerVehicle(
                request,
                ownerId,
                authenticatedUserId
        );

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