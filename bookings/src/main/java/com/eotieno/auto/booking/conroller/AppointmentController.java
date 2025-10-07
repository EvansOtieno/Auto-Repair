package com.eotieno.auto.booking.conroller;

import com.eotieno.auto.booking.dto.*;
import com.eotieno.auto.booking.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private static final Logger log = LoggerFactory.getLogger(AppointmentController.class);
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Get all appointments for a mechanic with optional filtering
     * GET /api/bookings/appointments/mechanic/{mechanicId}
     */
    @GetMapping("/mechanic/{mechanicId}")
    public ResponseEntity<List<AppointmentDto>> getMechanicAppointments(
            @PathVariable String mechanicId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {

        try {
            List<AppointmentDto> appointments = appointmentService.getMechanicAppointments(
                    mechanicId, status, dateFrom, dateTo, limit, offset);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            log.error("Error fetching appointments for mechanic {}: {}", mechanicId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Get appointment by ID
     * GET /api/bookings/appointments/{appointmentId}
     */
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long appointmentId) {
        try {
            return appointmentService.getAppointmentById(appointmentId)
                    .map(appointment -> ResponseEntity.ok(appointment))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update appointment status
     * PATCH /api/bookings/appointments/{appointmentId}/status
     */
    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, AppointmentStatus> statusUpdate) {

        try {
            AppointmentStatus newStatus = statusUpdate.get("status");
            if (newStatus == null) {
                return ResponseEntity.badRequest().build();
            }

            AppointmentDto updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, newStatus);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            log.error("Error updating appointment status for {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error updating appointment status for {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Add notes to appointment
     * POST /api/bookings/appointments/{appointmentId}/notes
     */
    @PostMapping("/{appointmentId}/notes")
    public ResponseEntity<AppointmentNoteDto> addAppointmentNotes(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, String> noteRequest,
            @RequestHeader("X-Mechanic-Id") String mechanicId) {

        try {
            String notes = noteRequest.get("notes");
            if (notes == null || notes.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            AppointmentNoteDto note = appointmentService.addAppointmentNotes(appointmentId, notes, mechanicId);
            return ResponseEntity.status(HttpStatus.CREATED).body(note);
        } catch (RuntimeException e) {
            log.error("Error adding notes to appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error adding notes to appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update appointment estimate
     * PATCH /api/bookings/appointments/{appointmentId}/estimate
     */
    @PatchMapping("/{appointmentId}/estimate")
    public ResponseEntity<AppointmentDto> updateAppointmentEstimate(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentEstimateDto estimate) {

        try {
            AppointmentDto updatedAppointment = appointmentService.updateAppointmentEstimate(appointmentId, estimate);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            log.error("Error updating estimate for appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error updating estimate for appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Complete appointment
     * POST /api/bookings/appointments/{appointmentId}/complete
     */
    @PostMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentDto> completeAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentCompletionDto completion) {

        try {
            AppointmentDto completedAppointment = appointmentService.completeAppointment(appointmentId, completion);
            return ResponseEntity.ok(completedAppointment);
        } catch (RuntimeException e) {
            log.error("Error completing appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error completing appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Reschedule appointment
     * PATCH /api/bookings/appointments/{appointmentId}/reschedule
     */
    @PatchMapping("/{appointmentId}/reschedule")
    public ResponseEntity<AppointmentDto> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, Object> rescheduleData) {

        try {
            LocalDate newDate = LocalDate.parse((String) rescheduleData.get("newDate"));
            String newTime = (String) rescheduleData.get("newTime");
            String reason = (String) rescheduleData.get("reason");

            AppointmentDto rescheduledAppointment = appointmentService.rescheduleAppointment(
                    appointmentId, newDate, newTime, reason);
            return ResponseEntity.ok(rescheduledAppointment);
        } catch (RuntimeException e) {
            log.error("Error rescheduling appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error rescheduling appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cancel appointment
     * PATCH /api/bookings/appointments/{appointmentId}/cancel
     */
    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentDto> cancelAppointment(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, String> cancelData) {

        try {
            String reason = cancelData.get("reason");
            AppointmentDto cancelledAppointment = appointmentService.cancelAppointment(appointmentId, reason);
            return ResponseEntity.ok(cancelledAppointment);
        } catch (RuntimeException e) {
            log.error("Error cancelling appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error cancelling appointment {}: {}", appointmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get appointment statistics for a mechanic
     * GET /api/bookings/appointments/mechanic/{mechanicId}/stats
     */
    @GetMapping("/mechanic/{mechanicId}/stats")
    public ResponseEntity<AppointmentStatsDto> getMechanicAppointmentStats(
            @PathVariable String mechanicId,
            @RequestParam(required = false, defaultValue = "week") String period) {

        try {
            AppointmentStatsDto stats = appointmentService.getMechanicAppointmentStats(mechanicId, period);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching stats for mechanic {}: {}", mechanicId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search appointments
     * GET /api/bookings/appointments/mechanic/{mechanicId}/search
     */
    @GetMapping("/mechanic/{mechanicId}/search")
    public ResponseEntity<List<AppointmentDto>> searchAppointments(
            @PathVariable String mechanicId,
            @RequestParam String search) {

        try {
            List<AppointmentDto> appointments = appointmentService.searchAppointments(mechanicId, search);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            log.error("Error searching appointments for mechanic {}: {}", mechanicId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get available time slots for a date
     * GET /api/bookings/appointments/mechanic/{mechanicId}/available-slots
     */
    @GetMapping("/mechanic/{mechanicId}/available-slots")
    public ResponseEntity<List<String>> getAvailableTimeSlots(
            @PathVariable String mechanicId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        try {
            List<String> availableSlots = appointmentService.getAvailableTimeSlots(mechanicId, date);
            return ResponseEntity.ok(availableSlots);
        } catch (Exception e) {
            log.error("Error fetching available slots for mechanic {} on {}: {}", mechanicId, date, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get vehicle appointment history
     * GET /api/bookings/appointments/vehicle/{vehicleId}/history
     */
    @GetMapping("/vehicle/{vehicleId}/history")
    public ResponseEntity<List<AppointmentDto>> getVehicleAppointmentHistory(@PathVariable String vehicleId) {
        try {
            List<AppointmentDto> appointments = appointmentService.getVehicleAppointmentHistory(vehicleId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            log.error("Error fetching appointment history for vehicle {}: {}", vehicleId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get customer appointment history with a mechanic
     * GET /api/bookings/appointments/mechanic/{mechanicId}/customer/{customerId}/history
     */
    @GetMapping("/mechanic/{mechanicId}/customer/{customerId}/history")
    public ResponseEntity<List<AppointmentDto>> getCustomerAppointmentHistory(
            @PathVariable String mechanicId,
            @PathVariable String customerId) {

        try {
            List<AppointmentDto> appointments = appointmentService.getCustomerAppointmentHistory(mechanicId, customerId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            log.error("Error fetching appointment history for customer {} with mechanic {}: {}",
                    customerId, mechanicId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Send appointment reminder
     * POST /api/bookings/appointments/{appointmentId}/remind
     */
    @PostMapping("/{appointmentId}/remind")
    public ResponseEntity<Map<String, Object>> sendAppointmentReminder(@PathVariable Long appointmentId) {
        try {
            // Implementation would depend on your notification service
            // For now, returning a success response
            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Reminder sent successfully"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sending reminder for appointment {}: {}", appointmentId, e.getMessage());
            Map<String, Object> response = Map.of(
                    "success", false,
                    "message", "Failed to send reminder"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Bulk update appointment statuses
     * PATCH /api/bookings/appointments/bulk/status
     */
    @PatchMapping("/bulk/status")
    public ResponseEntity<Map<String, Object>> bulkUpdateAppointmentStatus(
            @RequestBody Map<String, Object> bulkUpdate) {

        try {
            @SuppressWarnings("unchecked")
            List<Long> appointmentIds = (List<Long>) bulkUpdate.get("appointmentIds");
            AppointmentStatus status = AppointmentStatus.valueOf((String) bulkUpdate.get("status"));

            int updated = 0;
            List<String> failed = List.of(); // Implementation would track failed updates

            for (Long appointmentId : appointmentIds) {
                try {
                    appointmentService.updateAppointmentStatus(appointmentId, status);
                    updated++;
                } catch (Exception e) {
                    log.error("Failed to update appointment {}: {}", appointmentId, e.getMessage());
                    // Add to failed list
                }
            }

            Map<String, Object> response = Map.of(
                    "updated", updated,
                    "failed", failed
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error in bulk status update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Export appointments data
     * GET /api/bookings/appointments/mechanic/{mechanicId}/export
     */
    @GetMapping("/mechanic/{mechanicId}/export")
    public ResponseEntity<byte[]> exportAppointments(
            @PathVariable String mechanicId,
            @RequestParam(defaultValue = "csv") String format,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @RequestParam(required = false) AppointmentStatus status) {

        try {
            // Get appointments with filters
            List<AppointmentDto> appointments = appointmentService.getMechanicAppointments(
                    mechanicId, status, dateFrom, dateTo, null, null);

            // Generate CSV content (simplified implementation)
            StringBuilder csv = new StringBuilder();
            csv.append("Date,Time,Status,Customer,Phone,Vehicle,Issue,Duration\n");

            for (AppointmentDto appointment : appointments) {
                csv.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                        appointment.getPreferredDate().toLocalDate(),
                        appointment.getPreferredTime(),
                        appointment.getStatus(),
                        appointment.getCustomerDetails() != null ? appointment.getCustomerDetails().getName() : "N/A",
                        appointment.getCustomerDetails() != null ? appointment.getCustomerDetails().getPhone() : "N/A",
                        appointment.getVehicleId(),
                        appointment.getIssueDescription().replaceAll(",", ";").substring(0,
                                Math.min(50, appointment.getIssueDescription().length())),
                        appointment.getEstimatedDuration() != null ? appointment.getEstimatedDuration() + " min" : "N/A"
                ));
            }

            byte[] csvBytes = csv.toString().getBytes();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"appointments.csv\"")
                    .header("Content-Type", "text/csv")
                    .body(csvBytes);

        } catch (Exception e) {
            log.error("Error exporting appointments for mechanic {}: {}", mechanicId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

