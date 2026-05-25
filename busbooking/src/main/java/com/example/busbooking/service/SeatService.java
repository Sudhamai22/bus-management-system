package com.example.busbooking.service;

import com.example.busbooking.entity.Seat;
import java.util.List;

public interface SeatService {
    List<Seat> getSeatsByBus(Long busId);
}