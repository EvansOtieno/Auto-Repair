package com.eotieno.auto.booking.repository;

import com.eotieno.auto.booking.entity.ServicePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePartRepository extends JpaRepository<ServicePart, Long> {
}
