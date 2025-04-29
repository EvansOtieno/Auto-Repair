package com.eotieno.auto.user.dto;

import com.eotieno.auto.user.model.Role;
import io.jsonwebtoken.Claims;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long id;
    private String token;  // JWT token
    private Instant expiry;
    private String username;
    private String email;
    private Set<String> roles = new HashSet<>();
}
