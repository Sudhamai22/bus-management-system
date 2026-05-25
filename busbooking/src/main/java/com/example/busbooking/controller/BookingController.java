package com.example.busbooking.controller;

import com.example.busbooking.entity.Booking;
import com.example.busbooking.enums.BookingStatus;
import com.example.busbooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository repo;

    @GetMapping
    public List<Booking> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @PostMapping("/book")
    public Booking book(@RequestBody Booking booking) {
        booking.setStatus(BookingStatus.BOOKED);
        return repo.save(booking);
    }

    @DeleteMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id) {
        Booking b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        b.setStatus(BookingStatus.CANCELLED);
        repo.save(b);

        return "Booking Cancelled";
    }
}