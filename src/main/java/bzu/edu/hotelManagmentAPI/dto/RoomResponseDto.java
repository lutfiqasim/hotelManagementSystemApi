package bzu.edu.hotelManagmentAPI.dto;

import lombok.Data;

@Data
public class RoomResponseDto {
    private Long id;
    private String status;
    private String className;
    private int numBeds;
    private Float price;
    private int floorNumber;
}
