package bzu.edu.hotelManagmentAPI.model;

import bzu.edu.hotelManagmentAPI.enums.PaymentMethod;
import bzu.edu.hotelManagmentAPI.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.OnHold;

    private Float amount;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    // calculated value found better way
    // @Column(name = "payment_amount")
    // private Float paymentAmount;
    // From internet
    @Transient
    public Float getAmount() {
        return reservation.getReservationRooms().stream().map(room -> room.
                        getRoom().getRoomClass().getPrice())
                .reduce(0f, Float::sum);
    }
}
