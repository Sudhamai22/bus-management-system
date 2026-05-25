package com.example.busbooking.service;

import com.example.busbooking.entity.Bus;
import java.util.List;

public interface BusService {
    Bus addBus(Bus bus);
    List<Bus> getAllBuses();
}