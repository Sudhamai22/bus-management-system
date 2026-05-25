package com.example.busbooking.service;

import com.example.busbooking.dto.LoginRequest;
import com.example.busbooking.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    String login(LoginRequest request);
}