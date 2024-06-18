package bzu.edu.hotelManagmentAPI.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RoomUpdateDto {
    @NotBlank
    private String status;

    @NotBlank
    private String className;

    @NotNull
    @Positive
    private Integer numBeds;

    @NotNull
    @Min(value = 0)
    private Float price;

    @NotNull
    @Min(value = 1)
    private Integer floorNumber;

    @NotNull
    private String roomNumber;


}