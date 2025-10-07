// Service Layer
package com.eotieno.auto.booking.service;

import com.eotieno.auto.booking.dto.*;
import com.eotieno.auto.booking.entity.Booking;
import com.eotieno.auto.booking.entity.AppointmentNote;
import com.eotieno.auto.booking.entity.ServicePart;
import com.eotieno.auto.booking.repository.AppointmentNoteRepository;
import com.eotieno.auto.booking.repository.BookingRepository;
import com.eotieno.auto.booking.repository.NotificationService;
import com.eotieno.auto.booking.repository.ServicePartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentService {

    private final BookingRepository bookingRepository;
    private final AppointmentNoteRepository appointmentNoteRepository;
    private final ServicePartRepository servicePartRepository;
    private final NotificationService notificationService; // For sending notifications

    /**
     * Get all appointments for a mechanic with optional filtering
     */
    public List<AppointmentDto> getMechanicAppointments(String mechanicId,
                                                        AppointmentStatus status,
                                                        LocalDate dateFrom,
                                                        LocalDate dateTo,
                                                        Integer limit,
                                                        Integer offset) {
        List<Booking> bookings;

        if (status != null && dateFrom != null && dateTo != null) {
            bookings = bookingRepository.findByServiceProviderIdAndStatusAndPreferredDateBetween(
                    mechanicId, status, dateFrom, dateTo);
        } else if (status != null) {
            bookings = bookingRepository.findByServiceProviderIdAndStatus(mechanicId, status);
        } else if (dateFrom != null && dateTo != null) {
            bookings = bookingRepository.findByServiceProviderIdAndPreferredDateBetween(
                    mechanicId, dateFrom, dateTo);
        } else {
            if (limit != null) {
                Pageable pageable = PageRequest.of(
                        offset != null ? offset / limit : 0,
                        limit,
                        Sort.by("preferredDate").descending()
                );
                bookings = bookingRepository.findByServiceProviderId(mechanicId, pageable).getContent();
            } else {
                bookings = bookingRepository.findByServiceProviderIdOrderByPreferredDateDesc(mechanicId);
            }
        }

        return bookings.stream()
                .map(this::convertToAppointmentDto)
                .collect(Collectors.toList());
    }

    /**
     * Get appointment by ID
     */
    public Optional<AppointmentDto> getAppointmentById(Long appointmentId) {
        return bookingRepository.findById(appointmentId)
                .map(this::convertToAppointmentDto);
    }

    /**
     * Update appointment status
     */
    public AppointmentDto updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Booking booking = bookingRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        AppointmentStatus oldStatus = booking.getStatus();
        booking.setStatus(status);

        // Set actual start time when status changes to IN_PROGRESS
        if (status == AppointmentStatus.IN_PROGRESS && oldStatus != AppointmentStatus.IN_PROGRESS) {
            booking.setActualStartTime(LocalDateTime.now());
        }

        Booking savedBooking = bookingRepository.save(booking);

        // Send notification to customer
        notificationService.sendStatusUpdateNotification(booking, oldStatus, status);

        log.info("Updated appointment {} status from {} to {}", appointmentId, oldStatus, status);

        return convertToAppointmentDto(savedBooking);
    }

    /**
     * Add notes to appointment
     */
    public AppointmentNoteDto addAppointmentNotes(Long appointmentId, String notes, String mechanicId) {
        Booking booking = bookingRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        AppointmentNote note = AppointmentNote.builder()
                .booking(booking)
                .note(notes)
                .createdBy(mechanicId)
                .build();

        AppointmentNote savedNote = appointmentNoteRepository.save(note);

        log.info("Added note to appointment {}: {}", appointmentId, notes.substring(0, Math.min(50, notes.length())));

        return convertToAppointmentNoteDto(savedNote);
    }

    /**
     * Update appointment estimate
     */
    public AppointmentDto updateAppointmentEstimate(Long appointmentId, AppointmentEstimateDto estimate) {
        Booking booking = bookingRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        booking.setEstimatedDuration(estimate.getEstimatedDuration());
        booking.setTotalAmount(estimate.getEstimatedCost());

        Booking savedBooking = bookingRepository.save(booking);

        log.info("Updated estimate for appointment {}: {}min, KES {}",
                appointmentId, estimate.getEstimatedDuration(), estimate.getEstimatedCost());

        return convertToAppointmentDto(savedBooking);
    }

    /**
     * Complete appointment
     */
    public AppointmentDto completeAppointment(Long appointmentId, AppointmentCompletionDto completion) {
        Booking booking = bookingRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Update completion details
        booking.setStatus(AppointmentStatus.COMPLETED);
        booking.setActualEndTime(LocalDateTime.now());
        booking.setCompletionNotes(completion.getCompletionNotes());
        booking.setCustomerRating(completion.getCustomerRating());
        booking.setTotalAmount(completion.getTotalAmount());

        // Service details
        booking.setServicePerformed(completion.getServicePerformed());
        booking.setWorkQuality(completion.getWorkQuality());
        booking.setCustomerSatisfied(completion.getCustomerSatisfied());
        booking.setFollowUpNeeded(completion.getFollowUpNeeded());
        booking.setFollowUpDate(completion.getFollowUpDate());
        booking.setFollowUpReason(completion.getFollowUpReason());

        // Billing details
        booking.setLaborCost(completion.getLaborCost());
        booking.setPartsCost(completion.getPartsCost());
        booking.setAdditionalCharges(completion.getAdditionalCharges());
        booking.setAdditionalChargesDescription(completion.getAdditionalChargesDescription());
        booking.setDiscount(completion.getDiscount());
        booking.setDiscountReason(completion.getDiscountReason());
        booking.setPaymentMethod(completion.getPaymentMethod());

        Booking savedBooking = bookingRepository.save(booking);

        // Save parts used
        if (completion.getPartsUsed() != null && !completion.getPartsUsed().isEmpty()) {
            List<ServicePart> parts = completion.getPartsUsed().stream()
                    .map(partDto -> ServicePart.builder()
                            .booking(savedBooking)
                            .partName(partDto.getPartName())
                            .quantity(partDto.getQuantity())
                            .unitPrice(partDto.getUnitPrice())
                            .totalPrice(partDto.getUnitPrice().multiply(BigDecimal.valueOf(partDto.getQuantity())))
                            .supplier(partDto.getSupplier())
                            .partNumber(partDto.getPartNumber())
                            .build())
                    .collect(Collectors.toList());

            servicePartRepository.saveAll(parts);
        }

        // Send completion notification
        notificationService.sendCompletionNotification(booking);

        log.info("Completed appointment {} with total amount: KES {}",
                appointmentId, completion.getTotalAmount());

        return convertToAppointmentDto(savedBooking);
    }

    /**
     * Reschedule appointment
     */
    public AppointmentDto rescheduleAppointment(Long appointmentId, LocalDate newDate,
                                                String newTime, String reason) {
        Booking booking = bookingRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        LocalDate oldDate = booking.getPreferredDate();
        LocalTime oldTime = booking.getPreferredTime();

        booking.setPreferredDate(newDate);
        booking.setPreferredTime(LocalTime.parse(newTime));
        booking.setRescheduleReason(reason);

        Booking savedBooking = bookingRepository.save(booking);

        // Send reschedule notification
        notificationService.sendRescheduleNotification(booking, oldDate, oldTime);

        log.info("Rescheduled appointment {} from {}/{} to {}/{}",
                appointmentId, oldDate, oldTime, newDate, newTime);

        return convertToAppointmentDto(savedBooking);
    }

    /**
     * Cancel appointment
     */
    public AppointmentDto cancelAppointment(Long appointmentId, String reason) {
        Booking booking = bookingRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        booking.setStatus(AppointmentStatus.CANCELLED);
        booking.setCancelReason(reason);

        Booking savedBooking = bookingRepository.save(booking);

        // Send cancellation notification
        notificationService.sendCancellationNotification(booking);

        log.info("Cancelled appointment {} with reason: {}", appointmentId, reason);

        return convertToAppointmentDto(savedBooking);
    }

    /**
     * Get appointment statistics
     */
    public AppointmentStatsDto getMechanicAppointmentStats(String mechanicId, String period) {
        LocalDate startDate = calculateStartDate(period);
        LocalDate endDate = LocalDate.now();

        return AppointmentStatsDto.builder()
                .totalAppointments(bookingRepository.countByServiceProviderId(mechanicId))
                .pendingCount(bookingRepository.countByServiceProviderIdAndStatus(mechanicId, AppointmentStatus.PENDING))
                .confirmedCount(bookingRepository.countByServiceProviderIdAndStatus(mechanicId, AppointmentStatus.CONFIRMED))
                .inProgressCount(bookingRepository.countByServiceProviderIdAndStatus(mechanicId, AppointmentStatus.IN_PROGRESS))
                .completedCount(bookingRepository.countByServiceProviderIdAndStatus(mechanicId, AppointmentStatus.COMPLETED))
                .cancelledCount(bookingRepository.countByServiceProviderIdAndStatus(mechanicId, AppointmentStatus.CANCELLED))
                .noShowCount(bookingRepository.countByServiceProviderIdAndStatus(mechanicId, AppointmentStatus.NO_SHOW))
                .todayAppointments(bookingRepository.countTodayAppointments(mechanicId, LocalDate.now()))
                .thisWeekRevenue(bookingRepository.sumRevenueByServiceProviderIdAndDateRange(mechanicId, startDate, endDate))
                .averageRating(bookingRepository.averageRatingByServiceProviderId(mechanicId))
                .build();
    }

    /**
     * Search appointments
     */
    public List<AppointmentDto> searchAppointments(String mechanicId, String searchTerm) {
        List<Booking> bookings = bookingRepository.searchAppointments(mechanicId, searchTerm);
        return bookings.stream()
                .map(this::convertToAppointmentDto)
                .collect(Collectors.toList());
    }

    /**
     * Get available time slots for a date
     */
    public List<String> getAvailableTimeSlots(String mechanicId, LocalDate date) {
        // Get booked slots for the date
        List<Booking> bookedAppointments = bookingRepository.findByServiceProviderIdAndPreferredDate(mechanicId, date);
        List<String> bookedSlots = bookedAppointments.stream()
                .map(booking -> booking.getPreferredTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());

        // All possible time slots
        List<String> allSlots = List.of("08:00", "09:00", "10:00", "11:00", "12:00",
                "13:00", "14:00", "15:00", "16:00", "17:00");

        // Return available slots
        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    /**
     * Get vehicle appointment history
     */
    public List<AppointmentDto> getVehicleAppointmentHistory(String vehicleId) {
        List<Booking> bookings = bookingRepository.findByVehicleIdOrderByPreferredDateDesc(vehicleId);
        return bookings.stream()
                .map(this::convertToAppointmentDto)
                .collect(Collectors.toList());
    }

    /**
     * Get customer appointment history
     */
    public List<AppointmentDto> getCustomerAppointmentHistory(String mechanicId, String customerId) {
        List<Booking> bookings = bookingRepository.findByServiceProviderIdAndCustomerIdOrderByPreferredDateDesc(
                mechanicId, customerId);
        return bookings.stream()
                .map(this::convertToAppointmentDto)
                .collect(Collectors.toList());
    }

    // Helper methods
    private LocalDate calculateStartDate(String period) {
        LocalDate now = LocalDate.now();
        return switch (period != null ? period : "week") {
            case "week" -> now.minusWeeks(1);
            case "month" -> now.minusMonths(1);
            case "year" -> now.minusYears(1);
            default -> now.minusWeeks(1);
        };
    }

    private AppointmentDto convertToAppointmentDto(Booking booking) {
        return AppointmentDto.builder()
                .id(booking.getId())
                .vehicleId(booking.getVehicleId())
                .customerId(booking.getCustomerId())
                .serviceProviderId(booking.getServiceProviderId())
                .preferredDate(booking.getPreferredDate().atTime(booking.getPreferredTime()))
                .preferredTime(booking.getPreferredTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .issueDescription(booking.getIssueDescription())
                .customerDetails(convertToCustomerDetailsDto(booking.getCustomerDetails()))
                .status(booking.getStatus())
                .estimatedDuration(booking.getEstimatedDuration())
                .actualStartTime(booking.getActualStartTime())
                .actualEndTime(booking.getActualEndTime())
                .completionNotes(booking.getCompletionNotes())
                .customerRating(booking.getCustomerRating())
                .totalAmount(booking.getTotalAmount())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .serviceNotes(booking.getNotes() != null ?
                        booking.getNotes().stream()
                                .map(this::convertToAppointmentNoteDto)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    private CustomerDetailsDto convertToCustomerDetailsDto(Booking.CustomerDetails customerDetails) {
        if (customerDetails == null) return null;

        return CustomerDetailsDto.builder()
                .userId(customerDetails.getUserId())
                .name(customerDetails.getName())
                .phone(customerDetails.getPhone())
                .email(customerDetails.getEmail())
                .build();
    }

    private AppointmentNoteDto convertToAppointmentNoteDto(AppointmentNote note) {
        return AppointmentNoteDto.builder()
                .id(note.getId())
                .note(note.getNote())
                .createdBy(note.getCreatedBy())
                .createdAt(note.getCreatedAt())
                .build();
    }
}