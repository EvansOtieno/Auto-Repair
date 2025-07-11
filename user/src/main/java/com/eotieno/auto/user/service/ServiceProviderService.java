package com.eotieno.auto.user.service;

import com.eotieno.auto.user.dto.LocationDTO;
import com.eotieno.auto.user.dto.serviceProvider.NearbySearchRequest;
import com.eotieno.auto.user.dto.serviceProvider.ServiceProviderDTO;
import com.eotieno.auto.user.dto.serviceProvider.ServiceProviderDetailsDTO;
import com.eotieno.auto.user.model.mechanic.*;
import com.eotieno.auto.user.repository.MechanicProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceProviderService {

    @Autowired
    private MechanicProfileRepository mechanicProfileRepository;

    /**
     * Get nearby service providers based on search criteria
     */
    public List<ServiceProviderDTO> getNearbyServiceProviders(NearbySearchRequest request) {
        log.info("Searching for nearby service providers with criteria: {}", request);

        // Convert radius from kilometers to meters for MongoDB
        Double radiusMeters = request.getRadius() * 1000;

        List<MechanicProfile> mechanics = findMechanicsBySearchCriteria(request, radiusMeters);

        // Apply additional filters and convert to DTOs
        return mechanics.stream()
                .filter(mechanic -> applyAdditionalFilters(mechanic, request))
                .map(mechanic -> convertToServiceProviderDTO(mechanic, request.getLatitude(), request.getLongitude()))
                .sorted(getSortComparator(request.getSortBy(), request.getSortDirection()))
                .skip((long) request.getPage() * request.getSize())
                .limit(request.getSize())
                .collect(Collectors.toList());
    }

    /**
     * Get detailed service provider information by ID
     */
    public ServiceProviderDetailsDTO getServiceProviderDetails(String providerId) {
        log.info("Fetching service provider details for ID: {}", providerId);

        Optional<MechanicProfile> profileOpt = mechanicProfileRepository.findById(providerId);

        if (profileOpt.isEmpty()) {
            log.warn("Service provider not found with ID: {}", providerId);
            return null;
        }

        return convertToServiceProviderDetailsDTO(profileOpt.get());
    }

    /**
     * Get nearby service providers by category
     */
    public List<ServiceProviderDTO> getNearbyServiceProvidersByCategory(
            Double latitude, Double longitude, Double radiusKm, ServiceCategory category) {

        log.info("Searching for nearby {} service providers at lat: {}, lng: {}, radius: {}km",
                category, latitude, longitude, radiusKm);

        NearbySearchRequest request = NearbySearchRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .radius(radiusKm)
                .category(category)
                .page(0)
                .size(50)
                .sortBy("distance")
                .sortDirection("ASC")
                .build();

        return getNearbyServiceProviders(request);
    }

    /**
     * Get emergency service providers nearby
     */
    public List<ServiceProviderDTO> getNearbyEmergencyServices(
            Double latitude, Double longitude, Double radiusKm) {

        log.info("Searching for nearby emergency services at lat: {}, lng: {}, radius: {}km",
                latitude, longitude, radiusKm);

        NearbySearchRequest request = NearbySearchRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .radius(radiusKm)
                .emergencyService(true)
                .page(0)
                .size(20)
                .sortBy("distance")
                .sortDirection("ASC")
                .build();

        return getNearbyServiceProviders(request);
    }

    /**
     * Get mobile service providers nearby
     */
    public List<ServiceProviderDTO> getNearbyMobileServices(
            Double latitude, Double longitude, Double radiusKm) {

        log.info("Searching for nearby mobile services at lat: {}, lng: {}, radius: {}km",
                latitude, longitude, radiusKm);

        NearbySearchRequest request = NearbySearchRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .radius(radiusKm)
                .mobileMechanic(true)
                .page(0)
                .size(20)
                .sortBy("rating")
                .sortDirection("DESC")
                .build();

        return getNearbyServiceProviders(request);
    }

    /**
     * Search for service providers by service name
     */
    public List<ServiceProviderDTO> searchServiceProvidersByService(
            Double latitude, Double longitude, String serviceName, Double radiusKm) {

        log.info("Searching for '{}' services at lat: {}, lng: {}, radius: {}km",
                serviceName, latitude, longitude, radiusKm);

        NearbySearchRequest request = NearbySearchRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .radius(radiusKm)
                .serviceName(serviceName)
                .page(0)
                .size(30)
                .sortBy("distance")
                .sortDirection("ASC")
                .build();

        return getNearbyServiceProviders(request);
    }

    /**
     * Get top-rated service providers nearby
     */
    public List<ServiceProviderDTO> getTopRatedNearbyServices(
            Double latitude, Double longitude, Double radiusKm, Integer minRating) {

        log.info("Searching for top-rated services (min rating: {}) at lat: {}, lng: {}, radius: {}km",
                minRating, latitude, longitude, radiusKm);

        NearbySearchRequest request = NearbySearchRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .radius(radiusKm)
                .minRating(minRating)
                .isVerified(true)
                .page(0)
                .size(15)
                .sortBy("rating")
                .sortDirection("DESC")
                .build();

        return getNearbyServiceProviders(request);
    }

    // PRIVATE HELPER METHODS

    /**
     * Find mechanics based on search criteria
     */
    private List<MechanicProfile> findMechanicsBySearchCriteria(NearbySearchRequest request, Double radiusMeters) {
        if (request.getCategory() != null) {
            return mechanicProfileRepository.findNearbyMechanicsByCategory(
                    request.getLatitude(),
                    request.getLongitude(),
                    radiusMeters,
                    request.getCategory()
            );
        } else if (request.getServiceName() != null && !request.getServiceName().trim().isEmpty()) {
            return mechanicProfileRepository.findNearbyMechanicsByServiceName(
                    request.getLatitude(),
                    request.getLongitude(),
                    radiusMeters,
                    request.getServiceName().trim()
            );
        } else if (request.getEmergencyService() != null && request.getEmergencyService()) {
            return mechanicProfileRepository.findNearbyEmergencyMechanics(
                    request.getLatitude(),
                    request.getLongitude(),
                    radiusMeters,
                    true
            );
        } else if (request.getMobileMechanic() != null && request.getMobileMechanic()) {
            return mechanicProfileRepository.findNearbyMobileMechanics(
                    request.getLatitude(),
                    request.getLongitude(),
                    radiusMeters,
                    true
            );
        } else {
            return mechanicProfileRepository.findNearbyMechanics(
                    request.getLatitude(),
                    request.getLongitude(),
                    radiusMeters
            );
        }
    }

    /**
     * Apply additional filters to mechanic profiles
     */
    private boolean applyAdditionalFilters(MechanicProfile mechanic, NearbySearchRequest request) {
        // Filter by business type
        if (request.getBusinessType() != null &&
                !request.getBusinessType().equalsIgnoreCase(mechanic.getBusinessType().name())) {
            return false;
        }

        // Filter by minimum rating
        if (request.getMinRating() != null &&
                (mechanic.getAverageRating() == null || mechanic.getAverageRating() < request.getMinRating())) {
            return false;
        }

        // Filter by verification status
        if (request.getIsVerified() != null &&
                !Objects.equals(mechanic.getIsVerified(), request.getIsVerified())) {
            return false;
        }

        // Additional filters for emergency and mobile service (if not already filtered in query)
        if (request.getEmergencyService() != null &&
                !Objects.equals(mechanic.getEmergencyService(), request.getEmergencyService())) {
            return false;
        }

        if (request.getMobileMechanic() != null &&
                !Objects.equals(mechanic.getMobileMechanic(), request.getMobileMechanic())) {
            return false;
        }

        return true;
    }

    /**
     * Convert MechanicProfile to ServiceProviderDTO
     */
    private ServiceProviderDTO convertToServiceProviderDTO(MechanicProfile mechanic, Double searchLat, Double searchLng) {
        GeoJsonPoint point = mechanic.getLocation() != null ? mechanic.getLocation().getCoordinates() : null;

        return ServiceProviderDTO.builder()
                .id(mechanic.getId())
                .name(mechanic.getBusinessName())
                .location(LocationDTO.builder()
                        .latitude(point != null ? point.getY() : null) // getY() = latitude
                        .longitude(point != null ? point.getX() : null) // getX() = longitude
                        .address(mechanic.getLocation() != null ? mechanic.getLocation().getAddress() : null)
                        .build())
                .services(mechanic.getServicesOffered() != null ?
                        mechanic.getServicesOffered().stream()
                                .map(ServiceOffered::getName)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .rating(mechanic.getAverageRating() != null ? mechanic.getAverageRating() : 0.0)
                .phoneNumber(mechanic.getContactPhone())
                .email(mechanic.getContactEmail())
                .workingHours(formatWorkingHours(mechanic.getWorkingHours()))
                .description(mechanic.getDescription())
                .build();
    }


    /**
     * Convert MechanicProfile to ServiceProviderDetailsDTO
     */
    private ServiceProviderDetailsDTO convertToServiceProviderDetailsDTO(MechanicProfile mechanic) {
        GeoJsonPoint point = mechanic.getLocation() != null ? mechanic.getLocation().getCoordinates() : null;

        return ServiceProviderDetailsDTO.builder()
                .id(mechanic.getId())
                .name(mechanic.getBusinessName())
                .location(LocationDTO.builder()
                        .latitude(point != null ? point.getY() : null) // getY() = latitude
                        .longitude(point != null ? point.getX() : null) // getX() = longitude
                        .address(mechanic.getLocation() != null ? mechanic.getLocation().getAddress() : null)
                        .build())
                .services(mechanic.getServicesOffered() != null ?
                        mechanic.getServicesOffered().stream()
                                .map(ServiceOffered::getName)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .rating(mechanic.getAverageRating() != null ? mechanic.getAverageRating() : 0.0)
                .phoneNumber(mechanic.getContactPhone())
                .email(mechanic.getContactEmail())
                .workingHours(formatWorkingHours(mechanic.getWorkingHours()))
                .description(mechanic.getDescription())
                // Additional details
                .businessType(mechanic.getBusinessType())
                .specializations(mechanic.getSpecializations() != null ? mechanic.getSpecializations() : new ArrayList<>())
                .yearsOfExperience(mechanic.getYearsOfExperience())
                .servicesOffered(mechanic.getServicesOffered() != null ? mechanic.getServicesOffered() : new ArrayList<>())
                .certifications(mechanic.getCertifications() != null ? mechanic.getCertifications() : new ArrayList<>())
                .emergencyService(mechanic.getEmergencyService() != null ? mechanic.getEmergencyService() : false)
                .mobileMechanic(mechanic.getMobileMechanic() != null ? mechanic.getMobileMechanic() : false)
                .website(mechanic.getWebsite())
                //.profileImageUrl(mechanic.getProfileImageUrl())
                .isVerified(mechanic.getIsVerified() != null ? mechanic.getIsVerified() : false)
                .totalReviews(mechanic.getTotalReviews() != null ? mechanic.getTotalReviews() : 0)
                .build();
    }

    /**
     * Format working hours into a readable string
     */
    private String formatWorkingHours(List<WorkingHours> workingHours) {
        if (workingHours == null || workingHours.isEmpty()) {
            return "Hours not specified";
        }

        List<WorkingHours> openDays = workingHours.stream()
                .filter(wh -> wh.getIsOpen() != null && wh.getIsOpen())
                .collect(Collectors.toList());

        if (openDays.isEmpty()) {
            return "Closed";
        }

        // Group by time and format
        Map<String, List<String>> timeGroups = openDays.stream()
                .collect(Collectors.groupingBy(
                        wh -> (wh.getOpenTime() != null ? wh.getOpenTime() : "00:00") +
                                " - " + (wh.getCloseTime() != null ? wh.getCloseTime() : "23:59"),
                        LinkedHashMap::new,
                        Collectors.mapping(WorkingHours::getDay, Collectors.toList())
                ));

        return timeGroups.entrySet().stream()
                .map(entry -> {
                    String timeRange = entry.getKey();
                    List<String> days = entry.getValue();
                    if (days.size() == 1) {
                        return days.get(0) + ": " + timeRange;
                    } else {
                        return days.get(0) + " - " + days.get(days.size() - 1) + ": " + timeRange;
                    }
                })
                .collect(Collectors.joining(", "));
    }

    /**
     * Get sort comparator based on sort criteria
     */
    private Comparator<ServiceProviderDTO> getSortComparator(String sortBy, String sortDirection) {
        Comparator<ServiceProviderDTO> comparator;

        switch (sortBy != null ? sortBy.toLowerCase() : "distance") {
            case "rating":
                comparator = Comparator.comparing(ServiceProviderDTO::getRating,
                        Comparator.nullsLast(Double::compare));
                break;
            case "name":
                comparator = Comparator.comparing(ServiceProviderDTO::getName,
                        Comparator.nullsLast(String::compareToIgnoreCase));
                break;
            case "distance":
            default:
                // For distance sorting, we'll calculate distance in the controller or use a custom field
                comparator = Comparator.comparing(ServiceProviderDTO::getName); // Fallback to name if distance not available
                break;
        }

        return "DESC".equalsIgnoreCase(sortDirection) ? comparator.reversed() : comparator;
    }

    /**
     * Calculate distance between two points using Haversine formula
     */
    public Double calculateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return null;
        }

        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
