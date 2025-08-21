package com.eotieno.auto.booking.conroller;

import com.eotieno.auto.booking.entity.Booking;
import com.eotieno.auto.booking.entity.BookingRequest;
import com.eotieno.auto.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create a new booking
    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
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
}

