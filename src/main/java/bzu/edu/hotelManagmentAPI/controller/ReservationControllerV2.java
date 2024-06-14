package bzu.edu.hotelManagmentAPI.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.service.ReservationService;

@RestController
@RequestMapping(value = "/api/reservation", headers = "X-API-Version=2")

public class ReservationControllerV2 extends ReservationController {

    public ReservationControllerV2(ReservationService reservationService) {
        super(reservationService);
    }

//    @PostMapping(value = "/api/reservation/user/{userId}", headers = "X-API-Version=2")
//    public ResponseEntity<?> getAllReservations(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) LocalDate time, @RequestParam(required = false) String name, @RequestParam(required = false) Long id) {
//        if (page == null) {
//            page = 0;
//        }
//        if (size == null) {
//            size = 20;
//        }
//        return ResponseEntity.ok(super.reservationService.getAllReservations(page, size, id, name, time));
//    }

}
