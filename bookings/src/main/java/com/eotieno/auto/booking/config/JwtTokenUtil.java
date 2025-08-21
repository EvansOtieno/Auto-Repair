package com.eotieno.auto.booking.config;


import com.eotieno.auto.booking.exceptions.JwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public Long getUserIdFromToken(String token) {
        String userId = extractAllClaims(token).get("userId", String.class);

        return Long.parseLong(userId);
    }


    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Validate token
    public Boolean validateToken(String token, String userName) {
        final String username = extractUsername(token);
        return (username.equals(userName) && !isTokenExpired(token));
    }

    // Extract username from token
    public String extractUsername(String token) {
        try {
            return extractAllClaims(token).getSubject();
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenException("Token has expired");
        } catch (MalformedJwtException ex) {
            throw new JwtTokenException("Invalid token format");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenException("Token type not supported");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenException("Token cannot be empty or null");
        } catch (JwtException ex) {
            throw new JwtTokenException("Invalid token");
        }
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new JwtTokenException("Token expired - but here are the claims: " + ex.getClaims());
        } catch (MalformedJwtException ex) {
            throw new JwtTokenException("Invalid JWT structure");
        } catch (UnsupportedJwtException ex) {
            throw new JwtTokenException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtTokenException("JWT string cannot be empty or null");
        } catch (JwtException ex) {
            throw new JwtTokenException("Failed to process JWT token");
        }
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}