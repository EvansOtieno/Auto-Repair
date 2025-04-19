package com.eotieno.auto.user.conroller;

import com.eotieno.auto.user.dto.UserResponse;
import com.eotieno.auto.user.exceptions.NotFoundException;
import com.eotieno.auto.user.model.Role;
import com.eotieno.auto.user.model.RoleType;
import com.eotieno.auto.user.model.User;
import com.eotieno.auto.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping("/{userId}/exists")
    public boolean userExists(@PathVariable Long userId) {
        return userRepository.existsById(userId);
    }

    @GetMapping("/{userId}")
    public UserResponse user(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User",userId.toString()));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail() == null ? "" : user.getPhoneNumber())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name()) // Convert RoleType to String
                        .collect(Collectors.toSet()))
                .build();
    }

    @GetMapping("/username/{userName}")
    public UserResponse user(@PathVariable String userName) {
        var user = userRepository.findByEmail(userName)
                .or(() -> userRepository.findByPhoneNumber(userName))
                .orElseThrow(() -> new NotFoundException("User",userName));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail() == null ? "" : user.getPhoneNumber())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name()) // Convert RoleType to String
                        .collect(Collectors.toSet()))
                .build();
    }

    @Operation(summary = "Get all roles for a user")
    @ApiResponse(responseCode = "200", description = "Returns all assigned roles")
    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<String>> getUserRoles(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name()) // Convert RoleType to String
                .collect(Collectors.toSet());

        return ResponseEntity.ok(roleNames);
    }
}