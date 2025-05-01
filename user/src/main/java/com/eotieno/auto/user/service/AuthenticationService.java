package com.eotieno.auto.user.service;

import com.eotieno.auto.user.dto.AuthRequest;
import com.eotieno.auto.user.dto.AuthResponse;
import com.eotieno.auto.user.dto.RegisterRequest;
import com.eotieno.auto.user.model.Role;
import com.eotieno.auto.user.model.RoleType;
import com.eotieno.auto.user.model.User;
import com.eotieno.auto.user.repository.RoleRepository;
import com.eotieno.auto.user.repository.UserRepository;
import com.eotieno.auto.user.security.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Fetch roles from DB
        /*Set<Role> roles = request.getRoles().stream()
                .map(roleType -> roleRepository.findByName(roleType)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleType)))
                .collect(Collectors.toSet());*/
        Set<Role> roles = Set.of(
                roleRepository.findByName(RoleType.valueOf(request.getRoles()))
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + request.getRoles()))
        );

        var user = User.builder()
                .roles(roles)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        // Authenticate via email OR phone
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getIdentifier(),
                        request.getPassword()
                )
        );
        // Find user by email or phone
        var user = userRepository.findByEmail(request.getIdentifier())
                .or(() -> userRepository.findByPhoneNumber(request.getIdentifier()))
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        Claims claims = jwtService.extractAllClaims(jwtToken);
        // Extract role names
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()).stream().map(RoleType::name).collect(Collectors.toSet());

        return AuthResponse.builder()
                .id(user.getId())
                .token(jwtToken)
                .expiry(claims.getExpiration().toInstant())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }
}