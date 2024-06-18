package bzu.edu.hotelManagmentAPI.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RoomPartialUpdateDto {

    private String status;

    private String className;

    private String roomNumber;

    @Positive
    private Integer numBeds;

    @Min(value = 0)
    private Float price;

    @Min(value = 1)
    private Integer floorNumber;

}
