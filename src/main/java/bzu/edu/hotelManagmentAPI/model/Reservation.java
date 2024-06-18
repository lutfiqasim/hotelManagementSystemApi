package bzu.edu.hotelManagmentAPI.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.lang.Nullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checkin_date")
    @NotNull
    private LocalDate checkinDate;

    @Column(name = "checkout_date")
    @NotNull
    private LocalDate checkoutDate;

    @Column(name = "num_adults")
    @NotNull
    @Min(value = 1, message = "At least one adult is required to reserve a room")
    private Integer numAdults;

    @Column(name = "num_children") 
    private Integer numChildren; //why do we need this??

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    
    @Nullable //add payment after the reservation is created
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationRoom> reservationRooms;

    // calculated value found better way
    // @Column(name = "payment_amount")
    // private Float paymentAmount;
    // From internet
    @Transient
    public Float getPaymentAmount() {
        return reservationRooms.stream().map(room -> room.
                        getRoom().getRoomClass().getPrice())
                .reduce(0f, Float::sum);
    }
}
