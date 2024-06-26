package bzu.edu.hotelManagmentAPI.model;

import java.time.LocalDate;

import bzu.edu.hotelManagmentAPI.enums.PaymentMethod;
import bzu.edu.hotelManagmentAPI.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date")
    LocalDate paymentDate;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.OnHold;


    public Payment(PaymentMethod paymentMethod, PaymentStatus paymentStatus) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        // this.amount = this.getAmount();
    }

    @Transient
    public Float getAmount(Reservation reservation) {
        return reservation.getReservationRooms().stream().map(room -> room.
                        getRoom().getRoomClass().getPrice())
                .reduce(0f, Float::sum);
    }
}
