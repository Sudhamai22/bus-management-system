package com.example.busbooking.service;

import com.example.busbooking.entity.Booking;

public interface BookingService {
    Booking book(Booking booking);
    String cancel(Long id);
}