package com.example.busbooking.service.impl;

import com.example.busbooking.entity.Seat;
import com.example.busbooking.repository.SeatRepository;
import com.example.busbooking.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {

    @Autowired
    private SeatRepository repo;

    @Override
    public List<Seat> getSeatsByBus(Long busId) {
        return repo.findAll()
                .stream()
                .filter(s -> s.getBus().getBusId().equals(busId))
                .toList();
    }
}