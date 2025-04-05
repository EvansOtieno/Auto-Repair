package com.eotieno.auto.user.repository;

import com.eotieno.auto.user.model.Role;
import com.eotieno.auto.user.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
    boolean existsByName(RoleType name);
}