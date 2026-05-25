package com.example.busbooking.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookingRequest {

    private Long userId;
    private Long routeId;
    private List<Long> seatIds;
    private double totalAmount;
}