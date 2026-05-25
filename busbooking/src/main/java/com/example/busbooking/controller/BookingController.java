package com.example.busbooking.controller;

import com.example.busbooking.entity.Booking;
import com.example.busbooking.entity.BookingSeat;
import com.example.busbooking.entity.Payment;
import com.example.busbooking.entity.Route;
import com.example.busbooking.entity.Seat;
import com.example.busbooking.enums.BookingStatus;
import com.example.busbooking.enums.PaymentStatus;
import com.example.busbooking.repository.BookingRepository;
import com.example.busbooking.repository.BookingSeatRepository;
import com.example.busbooking.repository.PaymentRepository;
import com.example.busbooking.repository.RouteRepository;
import com.example.busbooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository repo;

    @Autowired
    private RouteRepository routeRepo;

    @Autowired
    private SeatRepository seatRepo;

    @Autowired
    private BookingSeatRepository bookingSeatRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    @GetMapping
    public List<Booking> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @PostMapping("/book")
    public Booking book(@RequestBody Map<String, Object> payload) {
        // payload expected: { route: { routeId }, seats: [ids], totalAmount, payment: { ... } }
        Object routeObj = payload.get("route");
        Long routeId = null;
        if (routeObj instanceof Map) {
            Object rid = ((Map) routeObj).get("routeId");
            if (rid instanceof Number) routeId = ((Number) rid).longValue();
            else if (rid instanceof String) routeId = Long.parseLong((String) rid);
        } else if (payload.get("routeId") instanceof Number) {
            routeId = ((Number) payload.get("routeId")).longValue();
        }

        Route route = null;
        if (routeId != null) {
            route = routeRepo.findById(routeId).orElse(null);
        }

        Booking booking = new Booking();
        booking.setRoute(route);
        booking.setStatus(BookingStatus.BOOKED);
        // totalAmount mapping
        Object amt = payload.get("totalAmount") != null ? payload.get("totalAmount") : payload.get("amount");
        double total = 0.0;
        if (amt instanceof Number) total = ((Number) amt).doubleValue();
        else if (amt instanceof String) total = Double.parseDouble((String) amt);
        booking.setTotalAmount(total);

        Booking saved = repo.save(booking);

        // seats
        Object seatsObj = payload.get("seats");
        if (seatsObj instanceof List) {
            List seats = (List) seatsObj;
            for (Object s : seats) {
                Seat seat = null;
                Long seatId = null;
                if (s instanceof Number) seatId = ((Number) s).longValue();
                else if (s instanceof String) {
                    // try parse numeric id, otherwise treat as seat number
                    try {
                        seatId = Long.parseLong((String) s);
                    } catch (NumberFormatException e) {
                        seatId = null;
                    }
                }

                if (seatId != null) {
                    seat = seatRepo.findById(seatId).orElse(null);
                }

                // if seat not found by id, try find by seatNumber within the bus for this route
                if (seat == null && route != null) {
                    String target = String(s);
                    List<Seat> busSeats = seatRepo.findByBus_BusId(route.getBus().getBusId());
                    for (Seat bs : busSeats) {
                        if (bs.getSeatNumber().equals(target)) {
                            seat = bs;
                            break;
                        }
                    }
                }

                if (seat != null) {
                    BookingSeat bsEntity = new BookingSeat();
                    bsEntity.setBooking(saved);
                    bsEntity.setSeat(seat);
                    bookingSeatRepo.save(bsEntity);
                }
            }
        }

        // payment
        Object paymentObj = payload.get("payment");
        if (paymentObj instanceof Map) {
            Map pay = (Map) paymentObj;
            Payment p = new Payment();
            p.setBooking(saved);
            p.setAmount(total);
            p.setPaymentMethod(pay.get("cardHolder") != null ? "CARD" : "UNKNOWN");
            p.setPaymentStatus(PaymentStatus.COMPLETED);
            paymentRepo.save(p);
        }

        return saved;
    }

    @DeleteMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id) {
        Booking b = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        b.setStatus(BookingStatus.CANCELLED);
        repo.save(b);

        return "Booking Cancelled";
    }
}