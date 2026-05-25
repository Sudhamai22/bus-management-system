package com.example.busbooking.service.impl;

import com.example.busbooking.entity.Payment;
import com.example.busbooking.enums.PaymentStatus;
import com.example.busbooking.repository.PaymentRepository;
import com.example.busbooking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository repo;

    @Override
    public Payment pay(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        return repo.save(payment);
    }
}