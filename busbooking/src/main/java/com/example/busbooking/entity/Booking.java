package com.example.busbooking.entity;

import com.example.busbooking.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    private LocalDateTime bookingDate = LocalDateTime.now();

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}