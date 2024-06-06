package bzu.edu.hotelManagmentAPI.model;


import jakarta.persistence.*;
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

    @Column(name = "num_addults")
    @NotNull
    @Min(value = 1, message = "At least one adult is required to reserve a room")
    private Integer numAdults;

    @Column(name = "num_children")
    private Integer numChildren;

    @Column(name = "payment_status")
    private String paymentStatus = "OnHold";

    @Column(name = "payment_amount")
    private Float paymentAmount;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
