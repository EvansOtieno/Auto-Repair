package com.eotieno.auto.user.repository;


import com.eotieno.auto.user.model.car_owner.CarOwnerProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CarOwnerProfileRepository extends MongoRepository<CarOwnerProfile, String> {
    Optional<CarOwnerProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    void deleteByUserId(Long userId);
}