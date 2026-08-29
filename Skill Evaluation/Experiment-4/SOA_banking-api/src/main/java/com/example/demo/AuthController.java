package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping; // Inferred
import org.springframework.web.bind.annotation.RequestBody; // Inferred
import org.springframework.web.bind.annotation.RestController; // Inferred
import java.util.Map; // Inferred

@RestController
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {
        
        String username = request.get("username");
        String password = request.get("password");

        String token = authService.login(username, password);

        if (token == null) {
            return ResponseEntity
                .status(401)
                .body("Invalid username or password");
        }

        return ResponseEntity.ok(
            Map.of("token", token)
        );
    }
}