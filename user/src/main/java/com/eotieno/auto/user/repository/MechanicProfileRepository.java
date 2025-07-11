package com.eotieno.auto.user.repository;

import com.eotieno.auto.user.model.mechanic.BusinessType;
import com.eotieno.auto.user.model.mechanic.MechanicProfile;
import com.eotieno.auto.user.model.mechanic.ServiceCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MechanicProfileRepository extends MongoRepository<MechanicProfile, String> {

    // Find by user ID
    Optional<MechanicProfile> findByUserId(String userId);

    // Find by business name (case insensitive)
    List<MechanicProfile> findByBusinessNameContainingIgnoreCase(String businessName);

    // Find by business type
    List<MechanicProfile> findByBusinessType(BusinessType businessType);

    // Find verified profiles
    List<MechanicProfile> findByIsVerifiedTrue();

    // Find complete profiles
    List<MechanicProfile> findByIsProfileCompleteTrue();

    // Find verified and complete profiles
    List<MechanicProfile> findByIsVerifiedTrueAndIsProfileCompleteTrue();

    // Find by emergency service availability
    List<MechanicProfile> findByEmergencyServiceTrue();

    // Find mobile mechanics
    List<MechanicProfile> findByMobileMechanicTrue();

    // Find by state
    @Query("{'location.state': ?0}")
    List<MechanicProfile> findByState(String state);

    // Find by minimum rating
    List<MechanicProfile> findByAverageRatingGreaterThanEqual(Double minRating);

    // Find by years of experience range
    List<MechanicProfile> findByYearsOfExperienceBetween(Integer minYears, Integer maxYears);

    // Geospatial queries - find mechanics near a location
    @Query("{'location': {$near: {$geometry: {type: 'Point', coordinates: [?1, ?0]}, $maxDistance: ?2}}}")
    List<MechanicProfile> findByLocationNear(Double latitude, Double longitude, Double maxDistanceInMeters);

    // Find mechanics within a certain radius using Spring Data GeoSpatial
    List<MechanicProfile> findByLocationCoordinatesNear(Point location, Distance distance);

    // Complex query combining multiple criteria
    @Query("{'isVerified': true, 'isProfileComplete': true, 'averageRating': {$gte: ?0}, 'location.city': ?1}")
    List<MechanicProfile> findVerifiedMechanicsInCityWithMinRating(Double minRating, String city);

    // Find mechanics offering specific service and in specific location
    @Query("{'servicesOffered.category': ?0, 'location.city': ?1, 'isVerified': true}")
    List<MechanicProfile> findVerifiedMechanicsByServiceAndCity(ServiceCategory serviceCategory, String city);

    // Find top rated mechanics (limit results)
    List<MechanicProfile> findTop10ByIsVerifiedTrueOrderByAverageRatingDesc();


    // Custom queries for nearby search with additional filters
    @Query("{ 'location.coordinates': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }, " +
            "'isProfileComplete': true }")
    List<MechanicProfile> findNearbyMechanics(Double latitude, Double longitude, Double radiusMeters);

    @Query("{ 'location.coordinates': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }, " +
            "'isProfileComplete': true, " +
            "'location.coordinates': { $exists: true }, " +
            "'servicesOffered.category': ?3 }")
    List<MechanicProfile> findNearbyMechanicsByCategory(Double latitude, Double longitude, Double radiusMeters, ServiceCategory category);

    @Query("{ 'location.coordinates': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }, " +
            "'isProfileComplete': true, " +
            "'location.coordinates': { $exists: true }, " +
            "'servicesOffered.name': { $regex: ?3, $options: 'i' } }")
    List<MechanicProfile> findNearbyMechanicsByServiceName(Double latitude, Double longitude, Double radiusMeters, String serviceName);

    @Query("{ 'location.coordinates': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }, " +
            "'isProfileComplete': true, " +
            "'location.coordinates': { $exists: true }, " +
            "'emergencyService': ?3 }")
    List<MechanicProfile> findNearbyEmergencyMechanics(Double latitude, Double longitude, Double radiusMeters, Boolean emergencyService);

    @Query("{ 'location.coordinates': { $near: { $geometry: { type: 'Point', coordinates: [?1, ?0] }, $maxDistance: ?2 } }, " +
            "'isProfileComplete': true, " +
            "'location.coordinates': { $exists: true }, " +
            "'mobileMechanic': ?3 }")
    List<MechanicProfile> findNearbyMobileMechanics(Double latitude, Double longitude, Double radiusMeters, Boolean mobileMechanic);

    // Additional existing methods...
    @Query("{ 'location.city': { $regex: ?0, $options: 'i' } }")
    List<MechanicProfile> findByCity(String city);

    @Query("{ 'specializations': { $in: [?0] } }")
    List<MechanicProfile> findBySpecialization(String specialization);

    @Query("{ 'servicesOffered.category': ?0 }")
    List<MechanicProfile> findByServiceCategory(ServiceCategory category);

    @Query("{ 'businessName': { $regex: ?0, $options: 'i' }, 'isVerified': ?1 }")
    Page<MechanicProfile> findByBusinessNameRegexAndVerified(String businessName, Boolean isVerified, Pageable pageable);

    @Query("{ 'location.city': { $regex: ?0, $options: 'i' }, 'isVerified': true }")
    Long countVerifiedMechanicsByCity(String city);

    @Query("{ 'updatedAt': { $gte: ?0 } }")
    List<MechanicProfile> findRecentlyUpdated(LocalDateTime since);

}
