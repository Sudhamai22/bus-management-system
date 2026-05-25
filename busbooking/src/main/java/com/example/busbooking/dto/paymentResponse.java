package com.example.busbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;
    private String status;
    private double amount;
}