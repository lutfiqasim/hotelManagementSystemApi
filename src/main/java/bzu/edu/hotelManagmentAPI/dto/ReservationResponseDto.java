package bzu.edu.hotelManagmentAPI.dto;

import java.time.LocalDate;

public class ReservationResponseDto {
    private Long id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer numAdults;
    private Integer numChildren;
    private Float paymentAmount;
    private String paymentStatus;

    public ReservationResponseDto(Long id, LocalDate checkinDate, LocalDate checkoutDate, Integer numAdults, Integer numChildren, String paymentStatus, Float paymentAmount) {
        this.id = id;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.paymentStatus = paymentStatus;
        this.paymentAmount = paymentAmount;
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
}
