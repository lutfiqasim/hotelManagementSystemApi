package bzu.edu.hotelManagmentAPI.service.RoomServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.RoomResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.RoomPartialUpdateDto;
import bzu.edu.hotelManagmentAPI.dto.RoomRequestDto;
import bzu.edu.hotelManagmentAPI.dto.RoomResponseDto;
import bzu.edu.hotelManagmentAPI.dto.RoomUpdateDto;
import bzu.edu.hotelManagmentAPI.repository.RoomRepository;
import bzu.edu.hotelManagmentAPI.security.SecurityUtils;
import bzu.edu.hotelManagmentAPI.service.RoomService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImp implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomResponseAssembler roomResponseAssembler;

    @Autowired
    public RoomServiceImp(RoomRepository roomRepository, RoomResponseAssembler roomResponseAssembler) {
        this.roomRepository = roomRepository;
        this.roomResponseAssembler = roomResponseAssembler;
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getAllRooms() {
        // throw new UnsupportedOperationException("Contact support");
        List<EntityModel<RoomResponseDto>> rooms = roomRepository.findAll().stream().map(roomResponseAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(rooms);
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getAllRooms(Integer floorNo) {
        // throw new UnsupportedOperationException("Contact support");
        List<EntityModel<RoomResponseDto>> rooms = roomRepository.findByFloor(floorNo).stream().map(roomResponseAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(rooms);
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
        SecurityUtils.checkIfAdminAuthority();
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

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getRoomsBySize(Integer size) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoomsBySize'");
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> getRoomsByDate(String date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoomsByDate'");
    }
}
