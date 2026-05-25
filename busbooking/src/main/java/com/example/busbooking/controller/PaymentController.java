package com.example.busbooking.controller;

import com.example.busbooking.entity.Payment;
import com.example.busbooking.enums.PaymentStatus;
import com.example.busbooking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository repo;

    @PostMapping("/pay")
    public Payment pay(@RequestBody Payment payment) {
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        return repo.save(payment);
    }
}