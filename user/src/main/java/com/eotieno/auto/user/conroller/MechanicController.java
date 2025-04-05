package com.eotieno.auto.user.conroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mechanic")
@RequiredArgsConstructor
public class MechanicController {

    @GetMapping("/dashboard")
    public ResponseEntity<String> mechanicDashboard() {
        return ResponseEntity.ok("Welcome, Mechanic!");
    }

//    @PostMapping("/diagnose")
//    public ResponseEntity<RepairReport> diagnoseVehicle(@RequestBody DiagnosisRequest request) {
//        return ResponseEntity.ok(repairService.createDiagnosis(request));
//    }
}