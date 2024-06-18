package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PaymentRepository  extends JpaRepository<Payment, Long>{

    //TODO: add query to find reservation by paymentID
    @Query("SELECT r from Reservation r where r.payment.id = :paymentId")
    Payment findReservationByPaymentId(@Param("paymentId") Long paymentId);
}
