package bzu.edu.hotelManagmentAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import org.springframework.lang.Nullable;
import lombok.Data;

@Data
public class ReservationPaymentDto {

    @JsonProperty("payment_date")
    private LocalDate paymentDate;

    @JsonProperty("reservation_id")
    private Long reservationId;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("payment_status")
    private String paymentStatus;

    private Float amount;

    @Nullable //null for create
    private Long id;

}
