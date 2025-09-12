package com.eotieno.auto.booking.conroller;

import com.eotieno.auto.booking.dto.LocationDTO;
import com.eotieno.auto.booking.dto.ServiceRecordDto;
import com.eotieno.auto.booking.dto.UserDto;
import com.eotieno.auto.booking.dto.serviceProvider.ServiceProviderDetailsDTO;
import com.eotieno.auto.booking.entity.Booking;
import com.eotieno.auto.booking.entity.BookingRequest;
import com.eotieno.auto.booking.service.BookingService;
import com.eotieno.auto.booking.service.UserServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final UserServiceClient userServiceClient;
    private final BookingService bookingService;

    public BookingController(UserServiceClient userServiceClient, BookingService bookingService) {
        this.userServiceClient = userServiceClient;
        this.bookingService = bookingService;
    }

    // Create a new booking
    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/vehicle/{vin}")
    public ResponseEntity<List<ServiceRecordDto>> getAllVehicleBookings(@PathVariable String vin) {
        List<Booking> bookings = bookingService.getBookingByVehicle(vin);

        List<ServiceRecordDto> dtos = bookings.stream()
                .map(this::mapToDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }
    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String serviceProviderId
    ) {
        if (customerId != null) {
            return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
        } else if (serviceProviderId != null) {
            return ResponseEntity.ok(bookingService.getBookingsByProvider(serviceProviderId));
        } else {
            return ResponseEntity.ok(bookingService.getAllBookings());
        }
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // Update booking
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable Long id,
            @RequestBody Booking request
    ) {
        return ResponseEntity.ok(bookingService.updateBooking(id, request));
    }

    // Cancel booking
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    // Confirm booking
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return ResponseEntity.noContent().build();
    }

    // Delete booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    private ServiceRecordDto mapToDto(Booking booking) {
        ServiceProviderDetailsDTO providerDto;

        try {
            // Attempt to fetch provider details from external service
            providerDto = userServiceClient.getServiceProviderById(
                    booking.getServiceProviderId()
            );

        } catch (Exception e) {
            // Fallback dummy provider
            providerDto = ServiceProviderDetailsDTO.builder()
                    .id(booking.getServiceProviderId())
                    .name("Unknown Provider")
                    .phoneNumber("N/A")
                    .email("N/A")
                    .location(new LocationDTO(0.0,0.0,"Unknown"))
                    .services(List.of("General Service"))
                    .rating(0.0)
                    .workingHours("N/A")
                    .description("Provider details not available")
                    .isVerified(false)
                    .totalReviews(0)
                    .build();
        }

        return ServiceRecordDto.builder()
                .id(booking.getId())
                .vehicleId(booking.getVehicleId())
                .serviceDate(booking.getPreferredDate())
                .status(booking.getStatus().name().toLowerCase())
                .issueDescription(booking.getIssueDescription())
                .serviceProvider(providerDto) // now using full details DTO

                // dummy placeholders for fields not yet in Booking
                .serviceType("General Service")
                .servicesPerformed(List.of())
                .mechanicNotes("N/A")
                .totalCost(0.0)
                .invoiceUrl(null)
                .createdAt(LocalDateTime.now())   // swap with auditing if you track
                .updatedAt(LocalDateTime.now())
                .build();
    }


}

