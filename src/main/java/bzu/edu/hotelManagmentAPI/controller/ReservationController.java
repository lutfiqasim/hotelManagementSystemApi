package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(@PathVariable Long userId) {
        return reservationService.getUserReservations(userId);
    }
    
    @PostMapping("/{id}/pay")
    public ResponseEntity<String> payForReservation(@RequestBody ReservationPaymentDto paymentDto) {
        reservationService.payForReservation(paymentDto);
        return ResponseEntity.ok("Successful Payment");
    }


}
