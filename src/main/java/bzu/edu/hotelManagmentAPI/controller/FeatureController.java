package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.model.Feature;
import bzu.edu.hotelManagmentAPI.model.RoomClassFeature;
import bzu.edu.hotelManagmentAPI.service.FeatureServiceImp.FeatureServiceImp;
import bzu.edu.hotelManagmentAPI.service.RoomClassFeatureServImp.RoomClassFeatureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
public class FeatureController {
    private FeatureServiceImp featureService;
    private RoomClassFeatureServiceImp roomClassFeatureService;

    @Autowired
    public FeatureController(FeatureServiceImp featureService, RoomClassFeatureServiceImp roomClassFeatureService) {
        this.featureService = featureService;
        this.roomClassFeatureService = roomClassFeatureService;
    }

    @PostMapping
    public Feature createFeature(@RequestBody Feature feature) {
        return featureService.saveFeature(feature);
    }

    @GetMapping
    public List<Feature> getAllFeatures() {
        return featureService.getAllFeatures();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feature> getFeatureById(@PathVariable Long id) {
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{featureId}/room-class/{roomClassId}")
    public ResponseEntity<RoomClassFeature> addFeatureToRoomClass(@PathVariable Long featureId, @PathVariable Long roomClassId) {
        RoomClassFeature roomClassFeature = roomClassFeatureService.addFeatureToRoomClass(roomClassId, featureId);
        return ResponseEntity.ok(roomClassFeature);
    }
}
