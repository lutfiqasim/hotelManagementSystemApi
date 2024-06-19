package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.model.RoomClassFeature;
import bzu.edu.hotelManagmentAPI.service.RoomClassFeatureServImp.RoomClassFeatureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-class-features")
public class RoomClassFeatureController {

    private RoomClassFeatureServiceImp roomClassFeatureService;

    @Autowired
    public RoomClassFeatureController(RoomClassFeatureServiceImp roomClassFeatureService) {
        this.roomClassFeatureService = roomClassFeatureService;
    }

    @PostMapping
    public RoomClassFeature createRoomClassFeature(@RequestBody RoomClassFeature roomClassFeature) {
        return roomClassFeatureService.saveRoomClassFeature(roomClassFeature);
    }

    @GetMapping
    public List<RoomClassFeature> getAllRoomClassFeatures() {
        return roomClassFeatureService.getAllRoomClassFeatures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomClassFeature> getRoomClassFeatureById(@PathVariable Long id) {
        return roomClassFeatureService.getRoomClassFeatureById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomClassFeature(@PathVariable Long id) {
        roomClassFeatureService.deleteRoomClassFeature(id);
        return ResponseEntity.noContent().build();
    }
}