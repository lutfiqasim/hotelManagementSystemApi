package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.RoomPartialUpdateDto;
import bzu.edu.hotelManagmentAPI.dto.RoomRequestDto;
import bzu.edu.hotelManagmentAPI.dto.RoomResponseDto;
import bzu.edu.hotelManagmentAPI.dto.RoomUpdateDto;
import bzu.edu.hotelManagmentAPI.enums.RoomClassEnum;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface RoomService {
    CollectionModel<EntityModel<RoomResponseDto>> getAllRooms();

    /**
     * @param floorNo the floor number (not the floor id)
     * @param date    the date of the desired booking
     * @param size    how many people the room can accommodate
     * @return a collection of **available** rooms that match the given criteria
     * Note: this method is only available in API V2
     */
    CollectionModel<EntityModel<RoomResponseDto>> getAllRoomsPageable(Integer floorNo, Pageable pageable); //V2 only

    /**
     * @param size how many people the room can accommodate
     * @return a collection of rooms that can accommodate the given number of people
     * Note: this method is only available in API V2
     */
    CollectionModel<EntityModel<RoomResponseDto>> getRoomsBySize(Integer size, Integer page, Integer pageSize); //V2 only

    CollectionModel<EntityModel<RoomResponseDto>> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate);//, RoomClassEnum roomClass, Integer numOfBeds);

    EntityModel<RoomResponseDto> getRoomById(Long id);

    EntityModel<RoomResponseDto> addNewRoom(RoomRequestDto roomRequestDto);

    void deleteRoom(Long id);

    EntityModel<RoomResponseDto> updateRoom(Long id, RoomUpdateDto roomUpdateDto);

    EntityModel<RoomResponseDto> partialUpdateRoom(Long id, RoomPartialUpdateDto roomPartialUpdateDto);


}
