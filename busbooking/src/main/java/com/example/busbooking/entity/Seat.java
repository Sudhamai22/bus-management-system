package com.example.busbooking.entity;

import com.example.busbooking.enums.SeatType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
}