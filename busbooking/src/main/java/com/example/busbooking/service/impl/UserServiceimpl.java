package com.example.busbooking.service.impl;

import com.example.busbooking.entity.User;
import com.example.busbooking.repository.UserRepository;
import com.example.busbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    public User register(User user) {

        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        return repo.save(user);
    }

    @Override
    public User login(String email, String password) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return repo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}