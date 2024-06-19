package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.ReservationInvoicesResponse;
import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationUpdateDto;
import jakarta.validation.Valid;

import java.time.LocalDate;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ReservationService {

    EntityModel<ReservationResponseDto> checkInForReservation(Long reservationId);

    EntityModel<ReservationResponseDto> checkoutForReservation(Long reservationId);

    CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(Long userId);

    CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations(Long id, String name, LocalDate date);

    Page<ReservationResponseDto> getAllReservations(Long userId, String name, LocalDate checkinDate, LocalDate checkoutDate, Pageable pageable);

    EntityModel<ReservationPaymentDto> payForReservation(Long id);

    EntityModel<ReservationResponseDto> getReservationById(Long id);

    EntityModel<ReservationResponseDto> addRoomToReservation(Long id, Long roomId) throws BadRequestException;

    EntityModel<ReservationResponseDto> reviewReservation(@Valid ReservationRequestDto reservationRequestDto) throws BadRequestException;

    EntityModel<ReservationResponseDto> createReservation(ReservationRequestDto reservationRequestDto) throws BadRequestException;

    EntityModel<ReservationResponseDto> updateReservation(Long id, ReservationUpdateDto reservationUpdateDto) throws BadRequestException;

    EntityModel<ReservationResponseDto> cancelReservation(Long id);

    public EntityModel<ReservationResponseDto> bookRoom(ReservationRequestDto reservationRequestDto) throws BadRequestException;


    boolean isRoomAvailable(Long roomId, LocalDate checkinDate, LocalDate checkoutDate);

    void deleteReservation(Long id);

    CollectionModel<EntityModel<ReservationResponseDto>> getUserReservationsOnHold(Long userId);

    CollectionModel<EntityModel<ReservationResponseDto>> getUpcomingReservations(Long userId);

    EntityModel<ReservationInvoicesResponse> getReservationInvoice(Long reservationId) throws BadRequestException;

    Page<ReservationResponseDto> getReservationsByDate(LocalDate date, Pageable pageable);

    Page<ReservationResponseDto> getAllReservations(Long userId, LocalDate checkinDate, LocalDate checkoutDate, Pageable pageable);
}
