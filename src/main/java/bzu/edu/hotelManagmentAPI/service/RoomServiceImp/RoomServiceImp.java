package bzu.edu.hotelManagmentAPI.service.RoomServiceImp;

import bzu.edu.hotelManagmentAPI.dto.RoomPartialUpdateDto;
import bzu.edu.hotelManagmentAPI.dto.RoomRequestDto;
import bzu.edu.hotelManagmentAPI.dto.RoomResponseDto;
import bzu.edu.hotelManagmentAPI.dto.RoomUpdateDto;
import bzu.edu.hotelManagmentAPI.repository.RoomRepository;
import bzu.edu.hotelManagmentAPI.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public class RoomServiceImp implements RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImp(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getAllRooms() {
        throw new UnsupportedOperationException("Contact support");
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getAvailableRooms() {
        throw new UnsupportedOperationException("Contact support");

    }

    @Override
    public EntityModel<RoomResponseDto> getRoomById(Long id) {
        throw new UnsupportedOperationException("Contact support");
    }

    @Override
    public EntityModel<RoomResponseDto> addNewRoom(RoomRequestDto roomRequestDto) {
        throw new UnsupportedOperationException("Contact support");
    }

    @Override
    public void deleteRoom(Long id) {
        
        roomRepository.deleteById(id);
    }

    @Override
    public EntityModel<RoomResponseDto> updateRoom(Long id, RoomUpdateDto roomUpdateDto) {
        throw new UnsupportedOperationException("Contact support");

    }

    @Override
    public EntityModel<RoomResponseDto> partialUpdateRoom(Long id, RoomPartialUpdateDto roomPartialUpdateDto) {
        throw new UnsupportedOperationException("Contact support");
    }
}
