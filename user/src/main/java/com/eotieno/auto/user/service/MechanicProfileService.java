package com.eotieno.auto.user.service;


import com.eotieno.auto.user.model.mechanic.BusinessType;
import com.eotieno.auto.user.model.mechanic.MechanicProfile;
import com.eotieno.auto.user.model.mechanic.ServiceCategory;
import com.eotieno.auto.user.repository.MechanicProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MechanicProfileService {

    @Autowired
    private MechanicProfileRepository mechanicProfileRepository;

    // Basic CRUD operations
    public MechanicProfile save(MechanicProfile profile) {
        profile.setUpdatedAt(LocalDateTime.now());
        return mechanicProfileRepository.save(profile);
    }

    public Optional<MechanicProfile> findById(String id) {
        return mechanicProfileRepository.findById(id);
    }

    public Optional<MechanicProfile> findByUserId(String userId) {
        return mechanicProfileRepository.findByUserId(userId);
    }

    public Optional<MechanicProfile> findByIdOrUserId(String id, String userId) {
        return mechanicProfileRepository.findById(id)
                .or(() -> mechanicProfileRepository.findByUserId(userId));
    }

    public List<MechanicProfile> findAll() {
        return mechanicProfileRepository.findAll();
    }

    public void deleteById(String id) {
        mechanicProfileRepository.deleteById(id);
    }

    // Business logic methods
    public MechanicProfile createProfile(MechanicProfile profile) {
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        profile.setIsProfileComplete(checkIfProfileComplete(profile));
        return mechanicProfileRepository.save(profile);
    }

    public MechanicProfile updateProfile(String id, MechanicProfile updatedProfile) {
        return mechanicProfileRepository.findByUserId(id)
                .map(existingProfile -> {
                    // Update fields
                    existingProfile.setBusinessName(updatedProfile.getBusinessName());
                    existingProfile.setBusinessType(updatedProfile.getBusinessType());
                    existingProfile.setDescription(updatedProfile.getDescription());
                    existingProfile.setSpecializations(updatedProfile.getSpecializations());
                    existingProfile.setYearsOfExperience(updatedProfile.getYearsOfExperience());
                    existingProfile.setLocation(updatedProfile.getLocation());
                    existingProfile.setContactPhone(updatedProfile.getContactPhone());
                    existingProfile.setContactEmail(updatedProfile.getContactEmail());
                    existingProfile.setWebsite(updatedProfile.getWebsite());
                    existingProfile.setServicesOffered(updatedProfile.getServicesOffered());
                    existingProfile.setWorkingHours(updatedProfile.getWorkingHours());
                    existingProfile.setCertifications(updatedProfile.getCertifications());
                    existingProfile.setLicenseNumber(updatedProfile.getLicenseNumber());
                    existingProfile.setInsuranceProvider(updatedProfile.getInsuranceProvider());
                    existingProfile.setEmergencyService(updatedProfile.getEmergencyService());
                    existingProfile.setMobileMechanic(updatedProfile.getMobileMechanic());

                    // Update profile completeness
                    existingProfile.setIsProfileComplete(checkIfProfileComplete(existingProfile));
                    existingProfile.setUpdatedAt(LocalDateTime.now());

                    return mechanicProfileRepository.save(existingProfile);
                })
                .orElse(null);
    }

    // Search and filter methods
    public List<MechanicProfile> searchByBusinessName(String businessName) {
        return mechanicProfileRepository.findByBusinessNameContainingIgnoreCase(businessName);
    }

    public List<MechanicProfile> findByBusinessType(BusinessType businessType) {
        return mechanicProfileRepository.findByBusinessType(businessType);
    }

    public List<MechanicProfile> findVerifiedMechanics() {
        return mechanicProfileRepository.findByIsVerifiedTrueAndIsProfileCompleteTrue();
    }

    public List<MechanicProfile> findByCity(String city) {
        return mechanicProfileRepository.findByCity(city);
    }

    public List<MechanicProfile> findBySpecialization(String specialization) {
        return mechanicProfileRepository.findBySpecialization(specialization);
    }

    public List<MechanicProfile> findByServiceCategory(ServiceCategory category) {
        return mechanicProfileRepository.findByServiceCategory(category);
    }

    public List<MechanicProfile> findNearLocation(Double latitude, Double longitude, Double radiusKm) {
        Point location = new Point(longitude, latitude);
        Distance distance = new Distance(radiusKm, Metrics.KILOMETERS);
        return mechanicProfileRepository.findByLocationCoordinatesNear(location, distance);
    }

    public List<MechanicProfile> findEmergencyMechanics() {
        return mechanicProfileRepository.findByEmergencyServiceTrue();
    }

    public List<MechanicProfile> findMobileMechanics() {
        return mechanicProfileRepository.findByMobileMechanicTrue();
    }

    public List<MechanicProfile> findTopRatedMechanics() {
        return mechanicProfileRepository.findTop10ByIsVerifiedTrueOrderByAverageRatingDesc();
    }

    public Page<MechanicProfile> searchMechanics(String businessName, Boolean isVerified, Pageable pageable) {
        return mechanicProfileRepository.findByBusinessNameRegexAndVerified(businessName, isVerified, pageable);
    }

    // Profile management methods
    public MechanicProfile verifyProfile(String id) {
        return mechanicProfileRepository.findById(id)
                .map(profile -> {
                    profile.setIsVerified(true);
                    profile.setUpdatedAt(LocalDateTime.now());
                    return mechanicProfileRepository.save(profile);
                })
                .orElse(null);
    }

    public MechanicProfile updateRating(String id, Double newRating, Integer totalReviews) {
        return mechanicProfileRepository.findById(id)
                .map(profile -> {
                    profile.setAverageRating(newRating);
                    profile.setTotalReviews(totalReviews);
                    profile.setUpdatedAt(LocalDateTime.now());
                    return mechanicProfileRepository.save(profile);
                })
                .orElse(null);
    }

    // Helper method to check if profile is complete
    private Boolean checkIfProfileComplete(MechanicProfile profile) {
        return profile.getBusinessName() != null && !profile.getBusinessName().trim().isEmpty() &&
                profile.getDescription() != null && profile.getDescription().length() >= 20 &&
                profile.getLocation() != null &&
                profile.getContactPhone() != null && !profile.getContactPhone().trim().isEmpty() &&
                profile.getContactEmail() != null && !profile.getContactEmail().trim().isEmpty() &&
                profile.getServicesOffered() != null && !profile.getServicesOffered().isEmpty() &&
                profile.getWorkingHours() != null && !profile.getWorkingHours().isEmpty();
    }

    // Statistics methods
    public Long countVerifiedMechanicsByCity(String city) {
        return mechanicProfileRepository.countVerifiedMechanicsByCity(city);
    }

    public List<MechanicProfile> findRecentlyUpdatedProfiles(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return mechanicProfileRepository.findRecentlyUpdated(since);
    }
}
