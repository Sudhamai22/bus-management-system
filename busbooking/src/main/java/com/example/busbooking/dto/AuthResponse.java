package com.example.busbooking.dto;

public class AuthResponse {

    private String message;
    private String token;
    private String role;
    private Object user;

    public AuthResponse(String message, String token, String role, Object user) {
        this.message = message;
        this.token = token;
        this.role = role;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }
}