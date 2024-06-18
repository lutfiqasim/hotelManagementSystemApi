package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.*;

import java.time.LocalDate;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ReservationService {

    CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(Long userId);

    Page<EntityModel<ReservationResponseDto>> getAllReservations(Integer page, Integer size, Long id, String name, LocalDate date);

    CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations(Long id, String name, LocalDate date);

    void payForReservation(ReservationPaymentDto reservationPaymentDto);

    EntityModel<ReservationResponseDto> getReservationById(Long id);

    EntityModel<ReservationResponseDto> createReservation(ReservationRequestDto reservationRequestDto) throws BadRequestException;

    EntityModel<ReservationResponseDto> updateReservation(Long id, ReservationUpdateDto reservationUpdateDto);

    void deleteReservation(Long id);

    CollectionModel<EntityModel<ReservationResponseDto>> getUserReservationsOnHold(Long userId);

    CollectionModel<EntityModel<ReservationResponseDto>> getUpcomingReservations(Long userId);

    EntityModel<ReservationInvoicesResponse> getReservationInvoice(Long reservationId);
}
