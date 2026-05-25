package com.example.busbooking.controller;

import com.example.busbooking.entity.Bus;
import com.example.busbooking.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/buses")
public class BusController {

    @Autowired
    private BusRepository repo;

    @PostMapping
    public Bus addBus(@RequestBody Bus bus) {
        return repo.save(bus);
    }

    @GetMapping
    public List<Bus> getAll() {
        return repo.findAll();
    }
}