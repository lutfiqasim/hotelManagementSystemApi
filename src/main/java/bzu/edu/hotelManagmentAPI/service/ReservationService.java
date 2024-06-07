package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationUpdateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface ReservationService {

    CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(Long userId);

    Page<EntityModel<ReservationResponseDto>> getAllReservations(Integer page, Integer size, Long id, String name, LocalDate time);

    CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations(Long id, String name, LocalDate time);

    void payForReservation(ReservationPaymentDto reservationPaymentDto);

    EntityModel<ReservationResponseDto> getReservationById(Long id);

    EntityModel<ReservationResponseDto> createReservation(ReservationRequestDto reservationRequestDto) throws BadRequestException;

    EntityModel<ReservationResponseDto> updateReservation(Long id, ReservationUpdateDto reservationUpdateDto);

    void deleteReservation(Long id);

    CollectionModel<EntityModel<ReservationResponseDto>> getUserReservationsOnHold(Long userId);

    CollectionModel<EntityModel<ReservationResponseDto>> getUpcomingReservations(Long userId);

}
