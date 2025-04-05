package com.eotieno.auto.user.conroller;


import com.eotieno.auto.user.model.Role;
import com.eotieno.auto.user.model.RoleType;
import com.eotieno.auto.user.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct  // Runs after application startup
    public void initRoles() {
        for (RoleType roleType : RoleType.values()) {
            // Only save the role if it doesn't exist
            if (roleRepository.findByName(roleType).isEmpty()) {
                roleRepository.save(Role.builder().name(roleType).build());
            }
        }
    }
}