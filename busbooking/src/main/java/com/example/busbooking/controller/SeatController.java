package com.example.busbooking.controller;

import com.example.busbooking.entity.Seat;
import com.example.busbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatRepository repo;

    @GetMapping("/{busId}")
    public List<Seat> getSeats(@PathVariable Long busId) {
        return repo.findAll()
                .stream()
                .filter(s -> s.getBus().getBusId().equals(busId))
                .toList();
    }
}