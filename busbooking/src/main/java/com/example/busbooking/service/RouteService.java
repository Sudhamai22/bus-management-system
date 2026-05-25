package com.example.busbooking.service;

import com.example.busbooking.entity.Route;
import java.util.List;

public interface RouteService {
    Route addRoute(Route route);
    List<Route> getAllRoutes();
}