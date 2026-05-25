package com.example.busbooking.controller;

import com.example.busbooking.dto.AuthResponse;
import com.example.busbooking.dto.LoginRequest;
import com.example.busbooking.dto.RegisterRequest;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;
import com.example.busbooking.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:4173"})
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req) {

        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(req.getRole());
        user.setPhone(req.getPhone());

        repo.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", user.getFullName());
        profile.put("email", user.getEmail());
        profile.put("role", user.getRole());

        return new AuthResponse("User Registered Successfully", token, user.getRole().name(), profile);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isBlocked()) {
            throw new RuntimeException("User is blocked");
        }

        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> profile = new HashMap<>();
        profile.put("fullName", user.getFullName());
        profile.put("email", user.getEmail());
        profile.put("role", user.getRole());

        return new AuthResponse("Login Successful", token, user.getRole().name(), profile);
    }
}