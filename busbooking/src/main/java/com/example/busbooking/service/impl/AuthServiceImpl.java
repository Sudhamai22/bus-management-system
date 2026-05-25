package com.example.busbooking.service.impl;

import com.example.busbooking.dto.LoginRequest;
import com.example.busbooking.dto.RegisterRequest;
import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;
import com.example.busbooking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository repo;

    @Override
    public String register(RegisterRequest req) {

        if (repo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); // plain password
        user.setRole(req.getRole());
        user.setPhone(req.getPhone());

        repo.save(user);

        return "User Registered Successfully";
    }

    @Override
    public String login(LoginRequest req) {

        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isBlocked()) {
            throw new RuntimeException("User is blocked");
        }

        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return "Login Successful - Role: " + user.getRole();
    }
}