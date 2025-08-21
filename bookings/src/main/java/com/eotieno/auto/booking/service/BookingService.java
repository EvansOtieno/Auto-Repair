package com.eotieno.auto.booking.service;

import com.eotieno.auto.booking.entity.Booking;
import com.eotieno.auto.booking.repository.BookingRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Create a new booking (default status = PENDING)
    public Booking createBooking(Booking booking) {
        booking.setStatus(Booking.Status.PENDING);
        return bookingRepository.save(booking);
    }

    // Get all bookings (optionally with filters)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByCustomer(String customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getBookingsByProvider(String serviceProviderId) {
        return bookingRepository.findByServiceProviderId(serviceProviderId);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = getBookingById(id);
        booking.setPreferredDate(bookingDetails.getPreferredDate());
        booking.setPreferredTime(bookingDetails.getPreferredTime());
        booking.setIssueDescription(bookingDetails.getIssueDescription());
        booking.setCustomerDetails(bookingDetails.getCustomerDetails());
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(Booking.Status.CANCELLED);
        bookingRepository.save(booking);
    }

    public void confirmBooking(Long id) {
        Booking booking = getBookingById(id);
        booking.setStatus(Booking.Status.CONFIRMED);
        bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
