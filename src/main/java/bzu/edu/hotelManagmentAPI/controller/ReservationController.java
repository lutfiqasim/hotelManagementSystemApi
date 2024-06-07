package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationUpdateDto;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping()
    public ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?>  getUserReservations(@RequestHeader(value = "X-API-Version", required = false) String apiVersion, @PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getAllReservations(@RequestHeader(value = "X-API-Version", required = false) String apiVersion, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        if ("v2".equals(apiVersion) && page != null && size != null) {
            return ResponseEntity.ok(reservationService.getAllReservations(page, size));
        }
        else if ("v1".equals(apiVersion)) {
            return ResponseEntity.badRequest().body("Invalid API version");
        }
        else if ("v2".equals(apiVersion) && (page == null || size == null)) {
            return ResponseEntity.badRequest().body("Page and size are required");
        }
        return ResponseEntity.ok(reservationService.getAllReservations());
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReservationResponseDto>> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
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
    public ResponseEntity<ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>>> getOnHoldPaymentUserReservations(@PathVariable Long userId) {
        return new ResponseEntity<>(reservationService.getUserReservationsOnHold(userId), HttpStatus.OK);
    }

    @GetMapping("users/{userId}/upcoming")
    public ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getUpcomingReservations(@PathVariable Long userId) {
        return new ResponseEntity<>(reservationService.getUpcomingReservations(userId), HttpStatus.OK);
    }
}
