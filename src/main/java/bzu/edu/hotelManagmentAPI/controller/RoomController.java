package bzu.edu.hotelManagmentAPI.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bzu.edu.hotelManagmentAPI.service.RoomService;

@RestController
@RequestMapping(value = "/api/rooms", params = "version=1") // query versioning
public class RoomController {

    protected final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }




    @GetMapping("/available")
    public ResponseEntity<?> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAvailableRooms());
    }

    @GetMapping("/time/{time}")
    public ResponseEntity<?> getRoomsByTime(@PathVariable String date) {
        return ResponseEntity.ok(roomService.getRoomsByDate(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/size/{count}")
    public ResponseEntity<?> getRoomsBySize(@PathVariable Integer count) {
        return ResponseEntity.ok(roomService.getRoomsBySize(count));
    }





    
}
