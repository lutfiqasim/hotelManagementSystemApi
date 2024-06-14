package bzu.edu.hotelManagmentAPI.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationUpdateDto {

    @NotNull
    private LocalDate checkinDate;

    @NotNull
    private LocalDate checkoutDate;

    @NotNull
    @Min(value = 1, message = "At least one adult is required to reserve a room")
    private Integer numAdults;

    private Integer numChildren;

    @NotNull
    private Float paymentAmount;

    @NotNull
    private String paymentStatus;
}
