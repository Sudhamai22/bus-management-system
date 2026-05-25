package com.example.busbooking.dto;

import lombok.Data;

@Data
public class BusRequest {

    private String busNumber;
    private String busName;
    private String busType;
    private int totalSeats;
}