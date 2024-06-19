package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/reservations", headers = "X-API-Version=2")
public class ReservationControllerV2 {
    private final ReservationService reservationService;

    @Autowired
    public ReservationControllerV2(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ReservationResponseDto>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDate checkinDate,
            @RequestParam(required = false) LocalDate checkoutDate,
            @RequestParam(required = false) Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationResponseDto> reservations = reservationService.getAllReservations(userId, checkinDate, checkoutDate, pageable);
        return ResponseEntity.ok(reservations);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/today")
    public ResponseEntity<Page<ReservationResponseDto>> getTodayReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LocalDate today = LocalDate.now();
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationResponseDto> reservations = reservationService.getReservationsByDate(today, pageable);
        return ResponseEntity.ok(reservations);
    }
}

