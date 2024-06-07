package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.RoomPartialUpdateDto;
import bzu.edu.hotelManagmentAPI.dto.RoomRequestDto;
import bzu.edu.hotelManagmentAPI.dto.RoomResponseDto;
import bzu.edu.hotelManagmentAPI.dto.RoomUpdateDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface RoomService {
    CollectionModel<EntityModel<RoomResponseDto>> getAllRooms();

    CollectionModel<EntityModel<RoomResponseDto>> getAvailableRooms();

    EntityModel<RoomResponseDto> getRoomById(Long id);

    EntityModel<RoomResponseDto> addNewRoom(RoomRequestDto roomRequestDto);

    void deleteRoom(Long id);

    EntityModel<RoomResponseDto> updateRoom(Long id, RoomUpdateDto roomUpdateDto);

    EntityModel<RoomResponseDto> partialUpdateRoom(Long id, RoomPartialUpdateDto roomPartialUpdateDto);


}
