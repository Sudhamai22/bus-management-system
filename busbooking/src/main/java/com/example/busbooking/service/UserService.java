package com.example.busbooking.service;

import com.example.busbooking.entity.User;
import java.util.List;

public interface UserService {

    User register(User user);

    User login(String email, String password);

    List<User> getAllUsers();

    User getUserById(Long userId);
}