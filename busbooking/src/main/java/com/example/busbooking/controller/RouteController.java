package com.example.busbooking.controller;

import com.example.busbooking.entity.Route;
import com.example.busbooking.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteRepository repo;

    @PostMapping
    public Route addRoute(@RequestBody Route route) {
        return repo.save(route);
    }

    @GetMapping
    public List<Route> getAll() {
        return repo.findAll();
    }
}