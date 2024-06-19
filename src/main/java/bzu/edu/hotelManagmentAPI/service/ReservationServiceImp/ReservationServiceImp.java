package bzu.edu.hotelManagmentAPI.service.ReservationServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.ReservationResponseAssembler;
import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.dto.*;
import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationUpdateDto;
import bzu.edu.hotelManagmentAPI.enums.PaymentMethod;
import bzu.edu.hotelManagmentAPI.enums.PaymentStatus;
import bzu.edu.hotelManagmentAPI.enums.ReservationStatusEnum;
import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.*;
import bzu.edu.hotelManagmentAPI.repository.*;
import bzu.edu.hotelManagmentAPI.security.SecurityUtils;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import bzu.edu.hotelManagmentAPI.service.ReservationService;

@Service
public class ReservationServiceImp implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomStatusRepository roomStatusRepository;
    private final ReservationResponseAssembler reservationResponseAssembler;
    private final PaymentRepository paymentRepository;
    private final ReservationRoomRepository reservationRoomRepository;

    @Autowired
    public ReservationServiceImp(ReservationRepository reservationRepository, UserRepository userRepository, RoomRepository roomRepository, RoomStatusRepository roomStatusRepository, ReservationResponseAssembler reservationResponseAssembler, PaymentRepository paymentRepository, ReservationRoomRepository reservationRoomRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.reservationResponseAssembler = reservationResponseAssembler;
        this.roomRepository = roomRepository;
        this.roomStatusRepository = roomStatusRepository;
        this.paymentRepository = paymentRepository;
        this.reservationRoomRepository = reservationRoomRepository;
    }

    @Override
    public CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityUtils.checkIfSameUserOrAdmin(user);
        List<Reservation> reservationList = reservationRepository.findByUserEntity(user);
        if (reservationList.isEmpty()) {
            return CollectionModel.empty();
        }
        return reservationResponseAssembler.toCollectionModel(reservationList);
    }

    @Override
    public Page<EntityModel<ReservationResponseDto>> getAllReservations(Integer page, Integer size, Long id, String name, LocalDate time) {
        SecurityUtils.checkIfAdminAuthority();
        return reservationRepository.findWithIdNameDate(id, name, time, Pageable.ofSize(size).withPage(page)).map(reservationResponseAssembler::toModel);
    }

    @Override
    public CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations(Long id, String name, LocalDate time) {
        SecurityUtils.checkIfAdminAuthority();
        List<EntityModel<ReservationResponseDto>> reservations = reservationRepository.findWithIdNameDate(id, name, time).stream().map(reservationResponseAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(reservations);
    }

    @Override
    public EntityModel<ReservationPaymentDto> payForReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        if (reservation.getPayment().getPaymentStatus().equals(PaymentStatus.Paid)) {
            throw new IllegalArgumentException("Reservation already paid for");
        }
        reservation.getPayment().setPaymentStatus(PaymentStatus.Paid);
        Reservation reservation1 = reservationRepository.save(reservation);
        EntityModel<ReservationPaymentDto> reservationPaymentDtoEntityModel = EntityModel.
                of(mapReservationToReservationPaymentDto(reservation1));
        reservationPaymentDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getReservationById(reservation1.getId())).withSelfRel());
        try {
            reservationPaymentDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getReservationInvoice(reservation1.getId())).withRel("Invoice"));
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        return reservationPaymentDtoEntityModel;
    }

    @Override
    public EntityModel<ReservationResponseDto> getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        SecurityUtils.checkIfSameUserOrAdmin(reservation.getUserEntity());
        return reservationResponseAssembler.toModel(reservation);
    }

    @Override
    public EntityModel<ReservationResponseDto> reviewReservation(@Valid ReservationRequestDto reservationRequestDto) throws BadRequestException {
        UserEntity user = userRepository.findById(reservationRequestDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        checkRoomsAvailability(reservationRequestDto.getRoomIds());
        Reservation reservation = fromRequestToEntity(reservationRequestDto, user);
        EntityModel<ReservationResponseDto> model = reservationResponseAssembler.toModel(reservation);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).addReservation(reservationRequestDto)).withRel("makeReservation"));
        return model;
    }

    @Override
    public EntityModel<ReservationResponseDto> createReservation(ReservationRequestDto reservationRequestDto) throws BadRequestException {
        UserEntity user = userRepository.findById(reservationRequestDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Reservation reservation = fromRequestToEntity(reservationRequestDto, user);
        Payment payment = new Payment();
        ReservationPaymentDto paymentDto = reservationRequestDto.getPayment();

        // Validate payment status
        if (paymentDto.getPaymentStatus() == null) {
            throw new BadRequestException("Payment status cannot be null");
        }
        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentDto.getPaymentStatus());
            payment.setPaymentStatus(paymentStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid Payment Status");
        }

        // Validate payment method
        if (paymentDto.getPaymentMethod() == null) {
            throw new BadRequestException("Payment method cannot be null");
        }
        try {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentDto.getPaymentMethod());
            payment.setPaymentMethod(paymentMethod);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid Payment Method");
        }

        // Proceed with reservation
        reserveRoomsIfAvailable(reservationRequestDto.getRoomIds());
//        payment.setAmount(reservation.getPaymentAmount());
        reservation.setPayment(payment);
        if (reservationRequestDto.getCheckinDate().equals(LocalDate.now())) {
            reservation.setReservationStatusEnum(ReservationStatusEnum.ONGOING);
        }
        Reservation savedReservation = reservationRepository.save(reservation);
        saveReservationRooms(reservationRequestDto.getRoomIds(), reservation);
        return reservationResponseAssembler.toModel(savedReservation);
    }


    @Override
    public EntityModel<ReservationResponseDto> updateReservation(Long id, ReservationUpdateDto reservationUpdateDto) throws BadRequestException {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        reservation.setCheckinDate(reservationUpdateDto.getCheckinDate());
        reservation.setCheckoutDate(reservationUpdateDto.getCheckoutDate());
        reservation.setNumAdults(reservationUpdateDto.getNumAdults());
        reservation.setNumChildren(reservationUpdateDto.getNumChildren());

        // Update payment status if needed
        if (reservation.getPayment() != null) {
            try {
                PaymentStatus paymentStatus = PaymentStatus.valueOf(reservationUpdateDto.getPaymentStatus());
                reservation.getPayment().setPaymentStatus(paymentStatus);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid Payment Status");
            }
        }
        if (reservation.getCheckinDate().equals(LocalDate.now()) && reservation.getReservationStatusEnum().equals(ReservationStatusEnum.ONHOLD)) {
            reservation.setReservationStatusEnum(ReservationStatusEnum.ONGOING);
        }

        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationResponseAssembler.toModel(updatedReservation);
    }

    @Override
    public EntityModel<ReservationResponseDto> cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + ", not found"));
        UserEntity currUser = userRepository.findById(reservation.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("Reserved user not found"));
        SecurityUtils.checkIfSameUserOrAdmin(currUser);
        reservation.setReservationStatusEnum(ReservationStatusEnum.CANCELED);
        setReservedRoomsToAvailable(reservation.getReservationRooms());
        Reservation reservation1 = reservationRepository.save(reservation);
        return reservationResponseAssembler.toModel(reservation1);
    }

    @Override
    public void deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
        }
    }

    @Override
    public CollectionModel<EntityModel<ReservationResponseDto>> getUserReservationsOnHold(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityUtils.checkIfSameUserOrAdmin(user);
        List<Reservation> onHoldReservations = reservationRepository.findByUserEntityAndPaymentStatus(user, PaymentStatus.OnHold);
        if (onHoldReservations.isEmpty()) {
            return CollectionModel.empty();
        }
        return reservationResponseAssembler.toCollectionModel(onHoldReservations);
    }

    @Override
    public CollectionModel<EntityModel<ReservationResponseDto>> getUpcomingReservations(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityUtils.checkIfSameUserOrAdmin(user);
        //TODO: check status exception
        List<Reservation> upcomingReservations = reservationRepository.findByUserEntityIdAndCheckinDateAfter(user, LocalDate.now());
        if (upcomingReservations.isEmpty()) {
            return CollectionModel.empty();
        }
        return reservationResponseAssembler.toCollectionModel(upcomingReservations);
    }

    @Override
    public EntityModel<ReservationInvoicesResponse> getReservationInvoice(Long reservationId) throws BadRequestException {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        SecurityUtils.checkIfSameUserOrAdmin(reservation.getUserEntity());

        //Methodology: invoice after payment only
        assert reservation.getPayment() != null;
        if (!reservation.getPayment().getPaymentStatus().equals(PaymentStatus.Paid)) {
            throw new AccessDeniedException("Can't get invoice before paying the bell, check the reservation to see basic payment info");
        }
        ReservationInvoicesResponse reservationInvoicesResponse = new ReservationInvoicesResponse(reservation.getId(), reservation.getCheckinDate(), reservation.getCheckoutDate(), reservation.getNumAdults(), reservation.getNumChildren(), reservation.getPaymentAmount());

        EntityModel<ReservationInvoicesResponse> model = EntityModel.of(reservationInvoicesResponse);
        return model;
    }

    private Reservation fromRequestToEntity(ReservationRequestDto reservationRequestDto, UserEntity user) {
        Reservation reservation = new Reservation();
        reservation.setCheckinDate(reservationRequestDto.getCheckinDate());
        reservation.setCheckoutDate(reservationRequestDto.getCheckoutDate());
        reservation.setNumAdults(reservationRequestDto.getNumAdults());
        reservation.setNumChildren(reservationRequestDto.getNumChildren());
        reservation.setUserEntity(user);
        return reservation;
    }

    private void checkRoomsAvailability(List<Long> roomIds) throws BadRequestException {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        for (Room room : rooms) {
            if (room.getStatus().getStatusName().equals(RoomStatusEnum.RESERVED)) {
                throw new BadRequestException("Requested Room with id = " + room.getId() + ", Room number = " + room.getRoomNumber() + ", is already reserved");
            }
        }
    }

    private void reserveRoomsIfAvailable(List<Long> roomIds) throws BadRequestException {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        checkRoomsAvailability(roomIds);
        RoomStatus roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.RESERVED).orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.RESERVED.name()));
        for (Room room : rooms) {
            room.setStatus(roomStatus);
        }
        roomRepository.saveAll(rooms);
    }

    private void setReservedRoomsToAvailable(List<ReservationRoom> reservationRooms) {
        RoomStatus roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.AVAILABLE).orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.AVAILABLE.name()));
        for (ReservationRoom r :
                reservationRooms) {
            Room room = roomRepository.findById(r.getRoom().getId()).orElseThrow(() -> new ResourceNotFoundException("Room with id: " + r.getRoom().getId() + ", not found"));
            room.setStatus(roomStatus);
            roomRepository.save(room);
        }
    }

    private void saveReservationRooms(List<Long> roomIds, Reservation reservation) {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        for (Room room : rooms) {
            reservationRoomRepository.save(new ReservationRoom(reservation, room));
        }
    }

    private ReservationPaymentDto mapReservationToReservationPaymentDto(Reservation reservation) {
        ReservationPaymentDto paymentDto = new ReservationPaymentDto();
        if (reservation.getPayment() != null) {
            paymentDto.setId(reservation.getPayment().getId());
            paymentDto.setPaymentDate(LocalDate.now());
            paymentDto.setPaymentMethod(reservation.getPayment().getPaymentMethod().name());
            paymentDto.setPaymentStatus(reservation.getPayment().getPaymentStatus().name());
            paymentDto.setAmount(reservation.getPaymentAmount());
        }
        paymentDto.setReservationId(reservation.getId());
        return paymentDto;
    }

}
