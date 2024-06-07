package bzu.edu.hotelManagmentAPI.dto;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomRequestDto {
    @JsonProperty("class_name")
    private String className;
    @JsonProperty("num_beds")
    private int numBeds;
    private Float price;
    @JsonProperty("floor_number")
    private int floorNumber;
}
