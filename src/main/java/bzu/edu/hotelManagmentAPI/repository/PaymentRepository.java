package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PaymentRepository  extends JpaRepository<Payment, Long>{

    Payment findByReservationId(Long reservationId);

    @Query("SELECT p FROM Payment p WHERE p.reservation.userEntity.id = :customerId")
    Payment findByCustomerId(Long customerId);

}
