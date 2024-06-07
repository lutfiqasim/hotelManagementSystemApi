package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationUpdateDto;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface ReservationService {

    CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(Long userId);

    CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations();

    void payForReservation(ReservationPaymentDto reservationPaymentDto);

    EntityModel<ReservationResponseDto> getReservationById(Long id);

    EntityModel<ReservationResponseDto> createReservation(ReservationRequestDto reservationRequestDto) throws BadRequestException;

    EntityModel<ReservationResponseDto> updateReservation(Long id, ReservationUpdateDto reservationUpdateDto);

    void deleteReservation(Long id);

    ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getUserReservationsOnHold(Long userId);

    CollectionModel<EntityModel<ReservationResponseDto>> getUpcomingReservations(Long userId);

}
