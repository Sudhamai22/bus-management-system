package com.example.busbooking.service.impl;

import com.example.busbooking.entity.Route;
import com.example.busbooking.repository.RouteRepository;
import com.example.busbooking.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository repo;

    @Override
    public Route addRoute(Route route) {
        return repo.save(route);
    }

    @Override
    public List<Route> getAllRoutes() {
        return repo.findAll();
    }
}