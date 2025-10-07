package com.eotieno.auto.booking.repository;

import com.eotieno.auto.booking.dto.AppointmentStatus;
import com.eotieno.auto.booking.entity.Booking;

import java.time.LocalDate;
import java.time.LocalTime;

public interface NotificationService {
    void sendStatusUpdateNotification(Booking booking, AppointmentStatus oldStatus, AppointmentStatus newStatus);
    void sendCompletionNotification(Booking booking);
    void sendRescheduleNotification(Booking booking, LocalDate oldDate, LocalTime oldTime);
    void sendCancellationNotification(Booking booking);
    void sendReminderNotification(Booking booking);
}
