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

    // Find by city
    @Query("{'location.city': ?0}")
    List<MechanicProfile> findByCity(String city);

    // Find by state
    @Query("{'location.state': ?0}")
    List<MechanicProfile> findByState(String state);

    // Find by specialization
    @Query("{'specializations': {$in: [?0]}}")
    List<MechanicProfile> findBySpecialization(String specialization);

    // Find by service category
    @Query("{'servicesOffered.category': ?0}")
    List<MechanicProfile> findByServiceCategory(ServiceCategory category);

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

    // Paginated search by multiple criteria
    @Query("{'businessName': {$regex: ?0, $options: 'i'}, 'isVerified': ?1}")
    Page<MechanicProfile> findByBusinessNameRegexAndVerified(String businessNamePattern, Boolean isVerified, Pageable pageable);

    // Count verified mechanics by city
    @Query(value = "{'location.city': ?0, 'isVerified': true}", count = true)
    Long countVerifiedMechanicsByCity(String city);

    // Find top rated mechanics (limit results)
    List<MechanicProfile> findTop10ByIsVerifiedTrueOrderByAverageRatingDesc();

    // Find recently updated profiles
    @Query("{'updatedAt': {$gte: ?0}}")
    List<MechanicProfile> findRecentlyUpdated(java.time.LocalDateTime since);
}
