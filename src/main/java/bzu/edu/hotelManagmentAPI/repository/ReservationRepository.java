package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.enums.PaymentStatus;
import bzu.edu.hotelManagmentAPI.model.Reservation;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Method to find reservations by user Id
    List<Reservation> findByUserEntity(UserEntity userEntity);

    @Query("SELECT r FROM Reservation r WHERE (:id is null or r.userEntity.id = :id) AND (:name is null or r.userEntity.firstName = :name) AND (:date is null or r.checkinDate = :date)")
    List<Reservation> findWithIdNameDate(Long id, String name, LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE (:id is null or r.userEntity.id = :id) AND (:name is null or r.userEntity.firstName = :name) AND (:checkinDate is null or r.checkinDate = :checkinDate) AND (:checkoutDate is null or r.checkoutDate = :checkoutDate)")
    Page<Reservation> findWithIdNameDate(Long id, String name, LocalDate checkinDate, LocalDate checkoutDate, Pageable pageable);

    // find reservations by user Id and payment status
    @Query("SELECT r FROM Reservation r WHERE r.userEntity = :userEntity AND r.payment.paymentStatus = :paymentStatus")
    List<Reservation> findByUserEntityAndPaymentStatus(@Param("userEntity") UserEntity userEntity, @Param("paymentStatus") PaymentStatus paymentStatus);

    // TODO: Fixed method to find upcoming reservations by user ID and check-in date(needs testing )
    List<Reservation> findByUserEntityAndCheckinDateAfter(UserEntity userEntity, LocalDate date);

    //    for pagination
    Page<Reservation> findByUserEntityId(Long userId, Pageable pageable);

    Page<Reservation> findByCheckinDateAndCheckoutDate(LocalDate checkinDate, LocalDate checkoutDate, Pageable pageable);

    Page<Reservation> findByCheckinDate(LocalDate date, Pageable pageable);

    Page<Reservation> findAll(Pageable pageable);
}
