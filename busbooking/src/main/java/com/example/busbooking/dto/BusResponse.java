package com.example.busbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BusResponse {

    private Long busId;
    private String busNumber;
    private String busName;
    private String busType;
    private int totalSeats;
}