package bzu.edu.hotelManagmentAPI.dto;

import java.time.LocalDate;

import bzu.edu.hotelManagmentAPI.enums.ReservationStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import bzu.edu.hotelManagmentAPI.model.Reservation;

import org.springframework.lang.Nullable;

public class ReservationResponseDto {
    private Long id;

    @JsonProperty("check_in_date")
    private LocalDate checkinDate;

    @JsonProperty("check_out_date")
    private LocalDate checkoutDate;

    @JsonProperty("num_adults")
    private Integer numAdults;

    @JsonProperty("num_children")
    private Integer numChildren;

    @JsonProperty("payment_amount")
    private Float paymentAmount;

    // @JsonProperty("payment_status")
    // private String paymentStatus;

    @JsonProperty("user_id")
    private Long userId;

    @Nullable //null when browsing reservation. Not null when booking
    ReservationPaymentDto payment;
    private ReservationStatusEnum reservationStatusEnum;

    public ReservationResponseDto(Long id, LocalDate checkinDate, LocalDate checkoutDate, Integer numAdults, Integer numChildren, Long userId) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.userId = userId;
    }

    public ReservationResponseDto(Long id, LocalDate checkinDate, LocalDate checkoutDate, Integer numAdults, Integer numChildren, Float paymentAmount, Long userId) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.paymentAmount = paymentAmount;
        this.userId = userId;
    }

    public ReservationResponseDto(Long id, LocalDate checkinDate, LocalDate checkoutDate, Integer numAdults, Integer numChildren, Float paymentAmount, Long userId, ReservationPaymentDto payment,ReservationStatusEnum reservationStatusEnum) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.paymentAmount = paymentAmount;
        this.userId = userId;
        this.payment = payment;
        this.reservationStatusEnum = reservationStatusEnum;
        this.payment.setAmount(paymentAmount);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Integer getNumAdults() {
        return numAdults;
    }

    public void setNumAdults(Integer numAdults) {
        this.numAdults = numAdults;
    }

    public Integer getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(Integer numChildren) {
        this.numChildren = numChildren;
    }

    public Float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ReservationPaymentDto getPayment() {
        return payment;
    }

    public void setPayment(ReservationPaymentDto payment) {
        this.payment = payment;
    }
}
