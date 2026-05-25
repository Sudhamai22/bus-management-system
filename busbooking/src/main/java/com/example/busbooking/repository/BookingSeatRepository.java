package com.example.busbooking.repository;

import com.example.busbooking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    List<BookingSeat> findByBooking_BookingId(Long bookingId);
}