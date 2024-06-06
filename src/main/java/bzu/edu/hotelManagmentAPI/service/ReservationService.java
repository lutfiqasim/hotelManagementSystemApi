package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.model.Reservation;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ReservationService {

    public abstract CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(Long userId);

    public abstract CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations();

    public abstract String payForReservation(ReservationPaymentDto reservationPaymentDto);

    public abstract EntityModel<ReservationResponseDto> getReservationById(Long id);

    public abstract EntityModel<ReservationResponseDto> createReservation(Reservation reservation);

    public abstract EntityModel<ReservationResponseDto> updateReservation(Long id, Reservation reservation);

    public abstract void deleteReservation(Long id);

}
