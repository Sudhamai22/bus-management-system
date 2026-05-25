package com.example.busbooking.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    private String source;
    private String destination;

    private LocalDate travelDate;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private double fare;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
}