package bzu.edu.hotelManagmentAPI.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RoomPartialUpdateDto {
    @NotBlank
    private String status;

    @NotBlank
    private String className;

    @Positive
    private Integer numBeds;

    @Min(value = 0)
    private Float price;

    @Positive
    private Integer floorNumber;

}
