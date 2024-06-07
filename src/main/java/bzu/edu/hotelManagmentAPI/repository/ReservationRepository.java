package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Method to find reservations by user Id
    List<Reservation> findByUserId(Long userId);

    // find reservations by user Id and payment status
    List<Reservation> findByUserIdAndPaymentStatus(Long userId, String paymentStatus);

    // find upcoming reservations by user ID and check-in date
    List<Reservation> findByUserIdAndCheckinDateAfterAndPaymentStatus(Long userId, LocalDate date, String paymentStatus);
}
