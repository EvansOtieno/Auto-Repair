package com.eotieno.auto.booking.repository;

import com.eotieno.auto.booking.dto.AppointmentStatus;
import com.eotieno.auto.booking.dto.AppointmentStatusDTO;
import com.eotieno.auto.booking.entity.AppointmentNote;
import com.eotieno.auto.booking.entity.Booking;
import com.eotieno.auto.booking.entity.ServicePart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings by customer
    List<Booking> findByCustomerId(String customerId);

    // Find all bookings by service provider
    List<Booking> findByServiceProviderId(String serviceProviderId);

    // Find bookings by status
    List<Booking> findByStatus(AppointmentStatusDTO status);

    // Find bookings by customer and status
    List<Booking> findByCustomerIdAndStatus(String customerId, AppointmentStatusDTO status);

    // Find bookings by service provider and date
    List<Booking> findByServiceProviderIdAndPreferredDate(String serviceProviderId, LocalDate preferredDate);

    // Optional: find by vehicle
    List<Booking> findByVehicleId(String vehicleId);

    // Find appointments by mechanic/service provider
    List<Booking> findByServiceProviderIdOrderByPreferredDateDesc(String serviceProviderId);

    Page<Booking> findByServiceProviderId(String serviceProviderId, Pageable pageable);

    // Filter by status and mechanic
    List<Booking> findByServiceProviderIdAndStatus(String serviceProviderId, AppointmentStatus status);

    // Filter by date range and mechanic
    List<Booking> findByServiceProviderIdAndPreferredDateBetween(
            String serviceProviderId, LocalDate startDate, LocalDate endDate);

    // Filter by status, date range and mechanic
    List<Booking> findByServiceProviderIdAndStatusAndPreferredDateBetween(
            String serviceProviderId, AppointmentStatus status, LocalDate startDate, LocalDate endDate);

    // Search appointments
    @Query("SELECT b FROM Booking b WHERE b.serviceProviderId = :serviceProviderId " +
            "AND (LOWER(b.customerDetails.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.vehicleId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(b.issueDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR b.customerDetails.phone LIKE CONCAT('%', :searchTerm, '%'))")
    List<Booking> searchAppointments(@Param("serviceProviderId") String serviceProviderId,
                                     @Param("searchTerm") String searchTerm);

    // Vehicle appointment history
    List<Booking> findByVehicleIdOrderByPreferredDateDesc(String vehicleId);

    // Customer appointment history with mechanic
    List<Booking> findByServiceProviderIdAndCustomerIdOrderByPreferredDateDesc(
            String serviceProviderId, String customerId);

    // Statistics queries
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.serviceProviderId = :serviceProviderId")
    Long countByServiceProviderId(@Param("serviceProviderId") String serviceProviderId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.serviceProviderId = :serviceProviderId AND b.status = :status")
    Long countByServiceProviderIdAndStatus(@Param("serviceProviderId") String serviceProviderId,
                                           @Param("status") AppointmentStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.serviceProviderId = :serviceProviderId AND b.preferredDate = :date")
    Long countTodayAppointments(@Param("serviceProviderId") String serviceProviderId,
                                @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.serviceProviderId = :serviceProviderId " +
            "AND b.status = 'COMPLETED' AND b.preferredDate BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueByServiceProviderIdAndDateRange(@Param("serviceProviderId") String serviceProviderId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(AVG(b.customerRating), 0) FROM Booking b WHERE b.serviceProviderId = :serviceProviderId " +
            "AND b.customerRating IS NOT NULL")
    Double averageRatingByServiceProviderId(@Param("serviceProviderId") String serviceProviderId);
}
