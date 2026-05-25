package com.example.busbooking.controller;

import com.example.busbooking.entity.Route;
import com.example.busbooking.entity.Bus;
import com.example.busbooking.repository.BusRepository;
import com.example.busbooking.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    @Autowired
    private RouteRepository repo;

    @Autowired
    private BusRepository busRepo;

    @PostMapping
    public Route addRoute(@RequestBody Route route) {
        return repo.save(route);
    }

    @GetMapping
    public List<Route> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Route getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));
    }

    @PutMapping("/{id}")
    public Route updateRoute(@PathVariable Long id, @RequestBody Route route) {
        Route existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));
        existing.setSource(route.getSource());
        existing.setDestination(route.getDestination());
        existing.setTravelDate(route.getTravelDate());
        existing.setDepartureTime(route.getDepartureTime());
        existing.setArrivalTime(route.getArrivalTime());
        existing.setFare(route.getFare());

        if (route.getBus() != null && route.getBus().getBusId() != null) {
            Bus bus = busRepo.findById(route.getBus().getBusId())
                    .orElseThrow(() -> new RuntimeException("Bus not found"));
            existing.setBus(bus);
        }

        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteRoute(@PathVariable Long id) {
        Route existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));
        repo.delete(existing);
    }
}