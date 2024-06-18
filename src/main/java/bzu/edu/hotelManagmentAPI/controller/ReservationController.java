package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.*;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/reservations", headers = "X-API-Version=1")
public class ReservationController {
    protected final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping
    public ResponseEntity<?> getAllReservations(@RequestParam(required = false) LocalDate time, @RequestParam(required = false) String name, @RequestParam(required = false) Long id) {
        return ResponseEntity.ok(reservationService.getAllReservations(id, name, time));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservationResponseDto>> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping("/{reservationId}/invoice")
    public ResponseEntity<EntityModel<ReservationInvoicesResponse>> getReservationInvoice(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.getReservationInvoice(reservationId));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<String> payForReservation(@RequestBody ReservationPaymentDto paymentDto) {
        reservationService.payForReservation(paymentDto);
        return ResponseEntity.ok("Successful Payment");
    }

    @PostMapping("")
    public ResponseEntity<EntityModel<ReservationResponseDto>> addReservation(@Valid @RequestBody ReservationRequestDto reservationRequestDto) throws BadRequestException {
        return new ResponseEntity<>(reservationService.createReservation(reservationRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ReservationResponseDto>> updateAReservation(@PathVariable Long id, @Valid ReservationUpdateDto reservationUpdateDto) {
        return new ResponseEntity<>(reservationService.updateReservation(id, reservationUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/users/{userId}/onHold")
    public ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getOnHoldPaymentUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getUserReservationsOnHold(userId));
    }

    @GetMapping("users/{userId}/upcoming")
    public ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getUpcomingReservations(@PathVariable Long userId) {
        return new ResponseEntity<>(reservationService.getUpcomingReservations(userId), HttpStatus.OK);
    }
}
