package com.eotieno.auto.user.model.car_owner;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "car_owner_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarOwnerProfile {
    @Id
    private String id;

    // Reference to PostgreSQL user ID
    private Long userId;

    // Personal Information
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String drivingLicenseNumber;

    // Contact Information (duplicated from User but can be extended)
    private String secondaryPhone;
    private String primaryPhone;
    private String email;

    // Address Information
    private Address address;

    // Emergency Contacts
    private List<EmergencyContact> emergencyContacts;

    // Billing Information
    private BillingInfo billingInfo;

    // Service Preferences
    private Boolean receiveMaintenanceReminders;
    private Boolean receivePromotions;
    private Boolean allowRatingRequests;
    private Boolean shareLocationForEmergency;
    private String preferredContactTime;
    private String specialNotes;
}

