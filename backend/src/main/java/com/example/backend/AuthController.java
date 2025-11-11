package com.example.backend.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        Map<String, Object> response = new HashMap<>();

        if (existingUser.isPresent()) {
            response.put("success", false);
            response.put("message", "Email already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        userRepository.save(user);
        response.put("success", true);
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        Map<String, Object> response = new HashMap<>();

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(user.getPassword())) {
            response.put("success", false);
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Optionally, generate a JWT token here
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("token", "dummy-jwt-token"); // Replace with real JWT later
        return ResponseEntity.ok(response);
    }
}
