package bzu.edu.hotelManagmentAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bzu.edu.hotelManagmentAPI.service.RoomService;

@RestController
@RequestMapping(value = "/api/rooms", params = "version=2") // query versioning
public class RoomControllerV2 extends RoomController {

    public RoomControllerV2(RoomService roomService) {
        super(roomService);
    }

    @GetMapping
    public ResponseEntity<?> getAllRooms(@RequestParam(value = "floor", required = false) Integer floor, @RequestParam(value = "date", required = false) String date, @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(roomService.getAllRooms(floor, date, size));
    }

    @GetMapping("/size/{count}")
    public ResponseEntity<?> getRoomsBySize(@PathVariable Integer count, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return ResponseEntity.ok(roomService.getRoomsBySize(count, page, pageSize));
    }
    
}
