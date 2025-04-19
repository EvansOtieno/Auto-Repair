package com.eotieno.auto.user.config;

import com.eotieno.auto.user.model.Role;
import com.eotieno.auto.user.model.RoleType;
import com.eotieno.auto.user.model.User;
import com.eotieno.auto.user.repository.RoleRepository;
import com.eotieno.auto.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${vehicle.service.email}")
    private String vehicleServiceEmail;

    @Value("${vehicle.service.password}")
    private String vehicleServicePassword;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            initRoles();
            initServiceAccount();
        } catch (Exception e) {
            log.error("Data initialization failed", e);
        }
    }

    private void initRoles() {
        for (RoleType roleType : RoleType.values()) {
            roleRepository.findByName(roleType)
                    .orElseGet(() -> {
                        log.info("Creating role: {}", roleType);
                        return roleRepository.save(
                                Role.builder()
                                        .name(roleType)
                                        .description("System-generated role")
                                        .build()
                        );
                    });
        }
    }

    private void initServiceAccount() {
        if (userRepository.existsByEmail(vehicleServiceEmail)) {
            log.info("Service account already exists");
            return;
        }

        Role serviceRole = roleRepository.findByName(RoleType.ROLE_SERVICES)
                .orElseThrow(() -> new IllegalStateException("ROLE_SERVICE not found"));

        User serviceAccount = User.builder()
                .email(vehicleServiceEmail)
                .password(passwordEncoder.encode(vehicleServicePassword))
                .roles(Set.of(serviceRole))
                .firstName("VEHICLE_SERVICE")
                .lastName("SYSTEM_ACCOUNT")
                .phoneNumber("+000000000000")
                .address("INTERNAL_USE_ONLY")
                .build();

        userRepository.save(serviceAccount);
        log.info("Created vehicle service account: {}", vehicleServiceEmail);
    }
}