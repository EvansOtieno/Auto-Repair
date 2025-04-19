package com.eotieno.auto.user.dto;

import io.jsonwebtoken.Claims;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;  // JWT token
    private Instant expiry;
}
