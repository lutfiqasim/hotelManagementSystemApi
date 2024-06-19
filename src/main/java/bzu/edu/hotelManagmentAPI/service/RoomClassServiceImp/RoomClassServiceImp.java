package bzu.edu.hotelManagmentAPI.service.RoomClassServiceImp;

import bzu.edu.hotelManagmentAPI.model.RoomClass;
import bzu.edu.hotelManagmentAPI.repository.RoomClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomClassServiceImp {

    private RoomClassRepository roomClassRepository;

    public RoomClassServiceImp(RoomClassRepository roomClassRepository) {
        this.roomClassRepository = roomClassRepository;
    }

    public RoomClass saveRoomClass(RoomClass roomClass) {
        return roomClassRepository.save(roomClass);
    }

    public List<RoomClass> getAllRoomClasses() {
        return roomClassRepository.findAll();
    }

    public Optional<RoomClass> getRoomClassById(Long id) {
        return roomClassRepository.findById(id);
    }

    public void deleteRoomClass(Long id) {
        roomClassRepository.deleteById(id);
    }
}