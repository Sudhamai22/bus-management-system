package com.example.busbooking.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponse {

    private Long bookingId;
    private String status;
    private double totalAmount;
    private LocalDateTime bookingDate;
    private List<String> seatNumbers;
}