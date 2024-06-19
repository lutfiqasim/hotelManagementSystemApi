package bzu.edu.hotelManagmentAPI.controller;

import java.time.LocalDate;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.RoomPartialUpdateDto;
import bzu.edu.hotelManagmentAPI.dto.RoomRequestDto;
import bzu.edu.hotelManagmentAPI.dto.RoomUpdateDto;
import bzu.edu.hotelManagmentAPI.enums.RoomClassEnum;
import bzu.edu.hotelManagmentAPI.model.RoomClass;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import bzu.edu.hotelManagmentAPI.service.RoomService;

@RestController
@RequestMapping(value = "/api/rooms", params = "version=1") // query versioning
public class RoomController {

    protected final RoomService roomService;
    protected final ReservationService  reservationService;

    @Autowired
    public RoomController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRooms(@RequestParam(required = false) LocalDate checkInDate, @RequestParam(required = false)  LocalDate checkOutDate){  //@RequestParam(required = false) RoomClassEnum roomClassEnum, @RequestParam(required = false) Integer numOfBeds){
        return ResponseEntity.ok(roomService.getAvailableRooms(checkInDate, checkOutDate));//, roomClassEnum,numOfBeds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> addNewRoom(@RequestBody RoomRequestDto roomRequestDto) {
        return new ResponseEntity<>(roomService.addNewRoom(roomRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("book")
    public ResponseEntity<?> bookRoom(@RequestBody ReservationRequestDto reservationRequestDto) throws BadRequestException {
        return ResponseEntity.ok(reservationService.bookRoom(reservationRequestDto));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody RoomUpdateDto roomUpdateDto) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomUpdateDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/update")
    public ResponseEntity<?> partialUpdateRoom(@PathVariable Long id, @RequestBody RoomPartialUpdateDto roomUpdateDto) {
        return ResponseEntity.ok(roomService.partialUpdateRoom(id, roomUpdateDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

}
