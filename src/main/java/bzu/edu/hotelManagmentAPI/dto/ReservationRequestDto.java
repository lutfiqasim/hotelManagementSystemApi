package bzu.edu.hotelManagmentAPI.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationRequestDto {

    @NotNull
    private LocalDate checkinDate;

    @NotNull
    private LocalDate checkoutDate;

    @NotNull
    @Min(value = 1, message = "At least one adult is required to reserve a room")
    private Integer numAdults;

    private Integer numChildren = 0;
    
    @NotNull
    private Long userId;

    @NotNull
    private List<Long> roomIds; //why list???

    private ReservationPaymentDto payment;

}
