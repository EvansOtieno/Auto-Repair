package com.eotieno.auto.booking.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "vehicle-service", url = "${vehicle.service.url}")
public interface VehicleServiceClient {
}
