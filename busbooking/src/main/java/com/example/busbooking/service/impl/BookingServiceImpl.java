package com.example.busbooking.service.impl;

import com.example.busbooking.entity.Booking;
import com.example.busbooking.enums.BookingStatus;
import com.example.busbooking.repository.BookingRepository;
import com.example.busbooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository repo;

    @Override
    public Booking book(Booking booking) {
        booking.setStatus(BookingStatus.BOOKED);
        return repo.save(booking);
    }

    @Override
    public String cancel(Long id) {
        Booking b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        b.setStatus(BookingStatus.CANCELLED);
        repo.save(b);

        return "Booking Cancelled";
    }
}