package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.*;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import jakarta.transaction.NotSupportedException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllReservations(@RequestParam(required = false) LocalDate time, @RequestParam(required = false) String name, @RequestParam(required = false) Long id) {
        return ResponseEntity.ok(reservationService.getAllReservations(id, name, time));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservationResponseDto>> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    // @PostMapping
    // public ResponseEntity<EntityModel<ReservationResponseDto>> reviewReservation(@Valid @RequestBody ReservationRequestDto reservationRequestDto) throws BadRequestException {
    //     return new ResponseEntity<>(reservationService.reviewReservation(reservationRequestDto), HttpStatus.CREATED);
    // }

    @GetMapping("/{reservationId}/invoice")
    public ResponseEntity<EntityModel<ReservationInvoicesResponse>> getReservationInvoice(@PathVariable Long reservationId) throws BadRequestException {
        return ResponseEntity.ok(reservationService.getReservationInvoice(reservationId));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<EntityModel<ReservationPaymentDto>> payForReservation(@PathVariable Long id) {
        reservationService.payForReservation(id);
        return ResponseEntity.ok(reservationService.payForReservation(id));
    }

    @PatchMapping("/{id}/rooms/{roomId}")
    public ResponseEntity<EntityModel<ReservationResponseDto>> addRoomToReservation(@PathVariable Long id, @PathVariable Long roomId) throws BadRequestException {
        return ResponseEntity.ok(reservationService.addRoomToReservation(id, roomId));
    }

    @PostMapping("")
    public ResponseEntity<EntityModel<ReservationResponseDto>> addReservation(@Valid @RequestBody ReservationRequestDto reservationRequestDto) throws BadRequestException {
        return new ResponseEntity<>(reservationService.createReservation(reservationRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ReservationResponseDto>> updateAReservation(@PathVariable Long id, @Valid ReservationUpdateDto reservationUpdateDto) throws BadRequestException {
        return new ResponseEntity<>(reservationService.updateReservation(id, reservationUpdateDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EntityModel<ReservationResponseDto>> cancelAReservation(@PathVariable Long id) throws NotSupportedException {
        throw new NotSupportedException("contact suport");
    }

    @GetMapping("/users/{userId}/onHold")
    public ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getOnHoldPaymentUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getUserReservationsOnHold(userId));
    }

    @PatchMapping("/{id}/checkIn")
    public ResponseEntity<EntityModel<ReservationResponseDto>> checkInForExistingReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.checkInForReservation(id));
    }

    @GetMapping("users/{userId}/upcoming")
    public ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getUpcomingReservations(@PathVariable Long userId) {
        return new ResponseEntity<>(reservationService.getUpcomingReservations(userId), HttpStatus.OK);
    }
}
