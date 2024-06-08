package bzu.edu.hotelManagmentAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import bzu.edu.hotelManagmentAPI.model.Room;
import lombok.Data;

@Data
public class RoomResponseDto {
    private Long id;
    private String status;

    @JsonProperty("class_name")
    private String className;

    @JsonProperty("num_beds")
    private int numBeds;

    private Float price;

    @JsonProperty("floor_number")
    private int floorNumber;

    public RoomResponseDto(Room roomEntity){
        this.id = roomEntity.getId();
        this.status = roomEntity.getStatus().getStatusName().name();
        this.className = roomEntity.getRoomClass().getClassName().name();
        this.numBeds = roomEntity.getRoomClass().getNumBeds();
        this.price = roomEntity.getRoomClass().getPrice();
        this.floorNumber = roomEntity.getFloor().getFloorNumber();
    }
}
