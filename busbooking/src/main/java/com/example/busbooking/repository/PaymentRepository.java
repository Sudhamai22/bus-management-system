package com.example.busbooking.repository;

import com.example.busbooking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBooking_BookingId(Long bookingId);
}