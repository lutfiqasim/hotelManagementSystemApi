package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.model.RoomClass;
import bzu.edu.hotelManagmentAPI.service.RoomClassServiceImp.RoomClassServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-classes")
public class RoomClassController {
    private RoomClassServiceImp roomClassService;

    @Autowired
    public RoomClassController(RoomClassServiceImp roomClassService) {
        this.roomClassService = roomClassService;
    }

    @PostMapping
    public RoomClass createRoomClass(@RequestBody RoomClass roomClass) {
        return roomClassService.saveRoomClass(roomClass);
    }

    @GetMapping
    public List<RoomClass> getAllRoomClasses() {
        return roomClassService.getAllRoomClasses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomClass> getRoomClassById(@PathVariable Long id) {
        return roomClassService.getRoomClassById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomClass(@PathVariable Long id) {
        roomClassService.deleteRoomClass(id);
        return ResponseEntity.noContent().build();
    }
}

