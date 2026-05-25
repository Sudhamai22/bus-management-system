package com.example.busbooking.service.impl;

import com.example.busbooking.entity.Bus;
import com.example.busbooking.repository.BusRepository;
import com.example.busbooking.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository repo;

    @Override
    public Bus addBus(Bus bus) {
        return repo.save(bus);
    }

    @Override
    public List<Bus> getAllBuses() {
        return repo.findAll();
    }
}