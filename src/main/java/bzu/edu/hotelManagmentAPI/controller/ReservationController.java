package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    

}
