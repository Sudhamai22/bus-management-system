package com.example.busbooking.controller;

import com.example.busbooking.entity.Booking;
import com.example.busbooking.repository.BookingRepository;
import com.example.busbooking.repository.BusRepository;
import com.example.busbooking.repository.RouteRepository;
import com.example.busbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private RouteRepository routeRepository;

    @GetMapping
    public Map<String, Object> getReports() {
        Map<String, Object> reports = new HashMap<>();

        long totalUsers = userRepository.count();
        long totalBookings = bookingRepository.count();
        long totalBuses = busRepository.count();
        long totalRoutes = routeRepository.count();

        double revenue = bookingRepository.findAll().stream()
                .mapToDouble(Booking::getTotalAmount)
                .sum();

        long cancelledBookings = bookingRepository.findAll().stream()
                .filter(booking -> booking.getStatus() != null && booking.getStatus().name().equals("CANCELLED"))
                .count();

        reports.put("totalUsers", totalUsers);
        reports.put("totalBookings", totalBookings);
        reports.put("totalBuses", totalBuses);
        reports.put("totalRoutes", totalRoutes);
        reports.put("revenue", revenue);
        reports.put("cancelledBookings", cancelledBookings);

        return reports;
    }
}