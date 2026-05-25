package com.example.busbooking.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.busbooking.entity.Bus;
import com.example.busbooking.repository.BusRepository;

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

    @GetMapping("/{id}")
    public Bus getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
    }

    @PutMapping("/{id}")
    public Bus updateBus(@PathVariable Long id, @RequestBody Bus bus) {
        Bus existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        existing.setBusNumber(bus.getBusNumber());
        existing.setBusName(bus.getBusName());
        existing.setBusType(bus.getBusType());
        existing.setTotalSeats(bus.getTotalSeats());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        Bus existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        repo.delete(existing);
        return ResponseEntity.noContent().build();
    }
}
