package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Reservation;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Method to find reservations by user Id
    List<Reservation> findByUserEntity(UserEntity userEntity);

    // find reservations by user Id and payment status
    List<Reservation> findByUserEntityAndPaymentStatus(UserEntity userEntity, String paymentStatus);

    // find upcoming reservations by user ID and check-in date
    List<Reservation> findByUserEntityIdAndCheckinDateAfter(UserEntity userEntity, LocalDate date);
}
