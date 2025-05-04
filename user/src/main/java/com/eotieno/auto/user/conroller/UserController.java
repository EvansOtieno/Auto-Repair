package com.eotieno.auto.user.conroller;

import com.eotieno.auto.user.dto.UserResponse;
import com.eotieno.auto.user.exceptions.NotFoundException;
import com.eotieno.auto.user.model.User;
import com.eotieno.auto.user.repository.UserRepository;
import com.eotieno.auto.user.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract the token from the Authorization header
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            // Validate the token
            boolean isValid = jwtService.isTokenValid(token);

            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

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

    @GetMapping("/{userIdAuthorized}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authHeader.substring(7); // Remove "Bearer " prefix
            if (!jwtService.isTokenValid(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User", userId.toString()));

            UserResponse response = UserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail() == null ? "" : user.getEmail())
                    .roles(user.getRoles().stream()
                            .map(role -> role.getName().name())
                            .collect(Collectors.toSet()))
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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