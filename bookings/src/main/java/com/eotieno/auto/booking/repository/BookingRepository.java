package com.eotieno.auto.booking.repository;
import com.eotieno.auto.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings by customer
    List<Booking> findByCustomerId(String customerId);

    // Find all bookings by service provider
    List<Booking> findByServiceProviderId(String serviceProviderId);

    // Find bookings by status
    List<Booking> findByStatus(Booking.Status status);

    // Find bookings by customer and status
    List<Booking> findByCustomerIdAndStatus(String customerId, Booking.Status status);

    // Find bookings by service provider and date
    List<Booking> findByServiceProviderIdAndPreferredDate(String serviceProviderId, LocalDate preferredDate);

    // Optional: find by vehicle
    List<Booking> findByVehicleId(String vehicleId);
}
