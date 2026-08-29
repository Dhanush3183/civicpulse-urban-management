package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final JwtUtil jwtUtil;

    public AccountController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/account/details")
    public ResponseEntity<?> getAccountDetails(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // 1. Check if the header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        // 2. Extract the token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        // 3. Validate the token
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        // 4. Extract the username and return the response
        String username = jwtUtil.extractUsername(token);
        
        return ResponseEntity.ok("Account Details for " + username + ": Balance = ₹50,000");
    }
}