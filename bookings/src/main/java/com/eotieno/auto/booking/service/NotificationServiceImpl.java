package com.eotieno.auto.booking.service;

import com.eotieno.auto.booking.entity.Booking;
import com.eotieno.auto.booking.dto.AppointmentStatus;
import com.eotieno.auto.booking.repository.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    // You can inject email service, SMS service, etc. here
    // private final EmailService emailService;
    // private final SmsService smsService;

    @Override
    public void sendStatusUpdateNotification(Booking booking, AppointmentStatus oldStatus, AppointmentStatus newStatus) {
        try {
            String customerName = booking.getCustomerDetails().getName();
            String customerPhone = booking.getCustomerDetails().getPhone();
            String customerEmail = booking.getCustomerDetails().getEmail();

            String message = createStatusUpdateMessage(booking, oldStatus, newStatus);

            log.info("Sending status update notification to {}: {}", customerName, message);

            // Implementation examples:
            // emailService.sendEmail(customerEmail, "Appointment Status Update", message);
            // smsService.sendSms(customerPhone, message);

            // For now, just log the notification
            log.info("Status notification sent to {} ({}): {}", customerName, customerEmail, message);

        } catch (Exception e) {
            log.error("Failed to send status update notification for appointment {}: {}",
                    booking.getId(), e.getMessage());
        }
    }

    @Override
    public void sendCompletionNotification(Booking booking) {
        try {
            String customerName = booking.getCustomerDetails().getName();
            String customerEmail = booking.getCustomerDetails().getEmail();

            String message = String.format(
                    "Hello %s, your service appointment has been completed successfully. " +
                            "Total amount: KES %.2f. Thank you for choosing our services!",
                    customerName, booking.getTotalAmount()
            );

            log.info("Sending completion notification to {}: {}", customerName, message);

            // emailService.sendEmail(customerEmail, "Service Completed", message);

        } catch (Exception e) {
            log.error("Failed to send completion notification for appointment {}: {}",
                    booking.getId(), e.getMessage());
        }
    }

    @Override
    public void sendRescheduleNotification(Booking booking, LocalDate oldDate, LocalTime oldTime) {
        try {
            String customerName = booking.getCustomerDetails().getName();
            String customerEmail = booking.getCustomerDetails().getEmail();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

            String message = String.format(
                    "Hello %s, your appointment has been rescheduled. " +
                            "Previous: %s at %s. " +
                            "New: %s at %s. " +
                            "Reason: %s",
                    customerName,
                    oldDate.format(dateFormatter),
                    oldTime.format(timeFormatter),
                    booking.getPreferredDate().format(dateFormatter),
                    booking.getPreferredTime().format(timeFormatter),
                    booking.getRescheduleReason() != null ? booking.getRescheduleReason() : "Schedule adjustment"
            );

            log.info("Sending reschedule notification to {}: {}", customerName, message);

            // emailService.sendEmail(customerEmail, "Appointment Rescheduled", message);

        } catch (Exception e) {
            log.error("Failed to send reschedule notification for appointment {}: {}",
                    booking.getId(), e.getMessage());
        }
    }

    @Override
    public void sendCancellationNotification(Booking booking) {
        try {
            String customerName = booking.getCustomerDetails().getName();
            String customerEmail = booking.getCustomerDetails().getEmail();

            String message = String.format(
                    "Hello %s, your appointment scheduled for %s has been cancelled. " +
                            "Reason: %s. We apologize for any inconvenience.",
                    customerName,
                    booking.getPreferredDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")),
                    booking.getCancelReason() != null ? booking.getCancelReason() : "Schedule conflict"
            );

            log.info("Sending cancellation notification to {}: {}", customerName, message);

            // emailService.sendEmail(customerEmail, "Appointment Cancelled", message);

        } catch (Exception e) {
            log.error("Failed to send cancellation notification for appointment {}: {}",
                    booking.getId(), e.getMessage());
        }
    }

    @Override
    public void sendReminderNotification(Booking booking) {
        try {
            String customerName = booking.getCustomerDetails().getName();
            String customerEmail = booking.getCustomerDetails().getEmail();
            String customerPhone = booking.getCustomerDetails().getPhone();

            String message = String.format(
                    "Reminder: You have an appointment tomorrow at %s for your vehicle %s. " +
                            "Please arrive on time. Contact us if you need to reschedule.",
                    booking.getPreferredTime().format(DateTimeFormatter.ofPattern("h:mm a")),
                    booking.getVehicleId()
            );

            log.info("Sending reminder notification to {}: {}", customerName, message);

            // emailService.sendEmail(customerEmail, "Appointment Reminder", message);
            // smsService.sendSms(customerPhone, message);

        } catch (Exception e) {
            log.error("Failed to send reminder notification for appointment {}: {}",
                    booking.getId(), e.getMessage());
        }
    }

    private String createStatusUpdateMessage(Booking booking, AppointmentStatus oldStatus, AppointmentStatus newStatus) {
        String customerName = booking.getCustomerDetails().getName();

        return switch (newStatus) {
            case CONFIRMED -> String.format(
                    "Hello %s, your appointment has been confirmed for %s at %s.",
                    customerName,
                    booking.getPreferredDate().format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
                    booking.getPreferredTime().format(DateTimeFormatter.ofPattern("h:mm a"))
            );
            case IN_PROGRESS -> String.format(
                    "Hello %s, your service has started. We'll keep you updated on the progress.",
                    customerName
            );
            case COMPLETED -> String.format(
                    "Hello %s, your service has been completed successfully! Thank you for choosing our services.",
                    customerName
            );
            case CANCELLED -> String.format(
                    "Hello %s, your appointment has been cancelled. We apologize for any inconvenience.",
                    customerName
            );
            case NO_SHOW -> String.format(
                    "Hello %s, we missed you at your scheduled appointment. Please contact us to reschedule.",
                    customerName
            );
            default -> String.format(
                    "Hello %s, your appointment status has been updated to: %s",
                    customerName, newStatus.toString()
            );
        };
    }
}