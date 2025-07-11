package com.eotieno.auto.user.service;

import com.eotieno.auto.user.exceptions.NotFoundException;
import com.eotieno.auto.user.model.car_owner.CarOwnerProfile;
import com.eotieno.auto.user.repository.CarOwnerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarOwnerProfileService {
    private final CarOwnerProfileRepository profileRepository;
    private final UserService userServiceClient;

    public CarOwnerProfile createProfile(CarOwnerProfile profile) {

        if (profileRepository.existsByUserId(profile.getUserId())) {
            throw new IllegalStateException("Profile already exists for this user");
        }

        profile.setUserId(profile.getUserId());
        return profileRepository.save(profile);
    }

    public CarOwnerProfile getProfile(String profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new NotFoundException("Car Owner Profile not found with id: ", profileId));
    }

    public CarOwnerProfile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Car Owner Profile not found with id: ", userId.toString()));
    }

    public CarOwnerProfile updateProfile(String profileId, CarOwnerProfile updatedProfile) {
        return profileRepository.findByUserId(Long.valueOf(profileId))
                .map(existingProfile -> {
                    updatedProfile.setId(existingProfile.getId());
                    updatedProfile.setUserId(existingProfile.getUserId());
                    return profileRepository.save(updatedProfile);
                })
                .orElseThrow(() -> new NotFoundException("Car Owner Profile not found with id: ", profileId));
    }

    public void deleteProfile(String profileId) {
        profileRepository.deleteById(profileId);
    }

    public void deleteProfileByUserId(Long userId) {
        profileRepository.deleteByUserId(userId);
    }
}