package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys; // Inferred
import org.springframework.stereotype.Component; // Inferred
import javax.crypto.SecretKey; // Inferred
import java.util.Date; // Inferred

@Component
public class JwtUtil {
    
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "mysecretkeymysecretkeymysecretkey123456".getBytes()
    );

    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 600000))
            .signWith(secretKey)
            .compact();
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract username
    public String extractUsername(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}