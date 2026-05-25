package com.example.busbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long busId;

    private String busNumber;
    private String busName;
    private String busType;
    private int totalSeats;
}