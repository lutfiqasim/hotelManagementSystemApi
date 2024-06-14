package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Reservation;

import org.springframework.data.domain.Pageable;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Method to find reservations by user Id
    List<Reservation> findByUserEntity(UserEntity userEntity);

    // @Query("SELECT r FROM Reservation r WHERE r.id = :id AND r.userEntity.name = :name AND r.checkinDate = :date")
    // List<Reservation> findByIdNameDate(Long id, String name, LocalDate date);

    // find reservations by user Id and payment status
    List<Reservation> findByUserEntityAndPaymentStatus(UserEntity userEntity, String paymentStatus);


    // find upcoming reservations by user ID and check-in date
    List<Reservation> findByUserEntityIdAndCheckinDateAfter(UserEntity userEntity, LocalDate date);
}
