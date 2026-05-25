package com.example.busbooking.dto;

import com.example.busbooking.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private String phone;
    private Role role;
}