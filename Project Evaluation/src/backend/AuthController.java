package com.civicpulse.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() == null) user.setRole("CITIZEN");
        
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        Optional<User> dbUser = userRepository.findByUsername(loginRequest.getUsername());
        
        if(dbUser.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), dbUser.get().getPassword())) {
            String token = jwtUtil.generateToken(dbUser.get().getUsername());
            return ResponseEntity.ok(token); // Returns the JWT Token
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}