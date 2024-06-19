package bzu.edu.hotelManagmentAPI.service.RoomClassFeatureServImp;

import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.Feature;
import bzu.edu.hotelManagmentAPI.model.RoomClass;
import bzu.edu.hotelManagmentAPI.model.RoomClassFeature;
import bzu.edu.hotelManagmentAPI.repository.FeatureRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomClassFeatureRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomClassFeatureServiceImp {

    private final RoomClassFeatureRepository roomClassFeatureRepository;

    private final RoomClassRepository roomClassRepository;

    private final FeatureRepository featureRepository;

    @Autowired
    public RoomClassFeatureServiceImp(RoomClassFeatureRepository roomClassFeatureRepository, RoomClassRepository roomClassRepository, FeatureRepository featureRepository) {
        this.roomClassFeatureRepository = roomClassFeatureRepository;
        this.roomClassRepository = roomClassRepository;
        this.featureRepository = featureRepository;
    }

    public RoomClassFeature saveRoomClassFeature(RoomClassFeature roomClassFeature) {
        return roomClassFeatureRepository.save(roomClassFeature);
    }

    public List<RoomClassFeature> getAllRoomClassFeatures() {
        return roomClassFeatureRepository.findAll();
    }

    public Optional<RoomClassFeature> getRoomClassFeatureById(Long id) {
        return roomClassFeatureRepository.findById(id);
    }

    public void deleteRoomClassFeature(Long id) {
        roomClassFeatureRepository.deleteById(id);
    }

    public RoomClassFeature addFeatureToRoomClass(Long roomClassId, Long featureId) {
        RoomClass roomClass = roomClassRepository.findById(roomClassId)
                .orElseThrow(() -> new ResourceNotFoundException("RoomClass not found"));
        Feature feature = featureRepository.findById(featureId)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found"));

        RoomClassFeature roomClassFeature = new RoomClassFeature(roomClass, feature);
        return roomClassFeatureRepository.save(roomClassFeature);
    }
}