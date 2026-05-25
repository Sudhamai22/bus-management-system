package com.example.busbooking.repository;

import com.example.busbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByBus_BusId(Long busId);
}