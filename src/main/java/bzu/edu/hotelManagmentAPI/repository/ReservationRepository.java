package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Reservation;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Method to find reservations by user Id
    List<Reservation> findByUserEntity(UserEntity userEntity);

    @Query("SELECT r FROM Reservation r WHERE (:id is null or r.id = :id) AND (:name is null or r.userEntity.firstName = :name) AND (:date is null or r.checkinDate = :date)")
    Page<Reservation> findWithIdNameDate(Long id, String name, LocalDate date, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE (:id is null or r.id = :id) AND (:name is null or r.userEntity.firstName = :name) AND (:date is null or r.checkinDate = :date)")
    List<Reservation> findWithIdNameDate(Long id, String name, LocalDate date);

    // find reservations by user Id and payment status
    List<Reservation> findByUserEntityAndPaymentStatus(UserEntity userEntity, String paymentStatus);

    // find upcoming reservations by user ID and check-in date
    List<Reservation> findByUserEntityIdAndCheckinDateAfter(UserEntity userEntity, LocalDate date);
}
