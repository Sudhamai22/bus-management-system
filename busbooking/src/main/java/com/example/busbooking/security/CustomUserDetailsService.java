package com.example.busbooking.security;

import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {

    @Autowired
    private UserRepository repo;

    public User loadUserByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}