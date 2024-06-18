package bzu.edu.hotelManagmentAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationPaymentDto {
    @JsonProperty("reservation_id")
    private Long reservationId;

    public ReservationPaymentDto() {
    }

    public ReservationPaymentDto(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
