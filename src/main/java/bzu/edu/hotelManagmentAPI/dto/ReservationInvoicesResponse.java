package bzu.edu.hotelManagmentAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationInvoicesResponse {
    @JsonProperty("reservation_id")
    private Long reservationId;
    @JsonProperty("checkin_date")
    private LocalDate checkinDate;
    @JsonProperty("checkout_date")
    private LocalDate checkoutDate;
    @JsonProperty("num_adults")
    private Integer numAdults;
    @JsonProperty("num_children")
    private Integer numChildren;
    @JsonProperty("payment_amount")
    private Float paymentAmount;

    public ReservationInvoicesResponse(Long id, LocalDate checkinDate, LocalDate checkoutDate, Integer numAdults, Integer numChildren, Float paymentAmount) {
        this.reservationId = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.paymentAmount = paymentAmount;
    }
}

