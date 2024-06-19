package bzu.edu.hotelManagmentAPI.service.ReservationServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.ReservationResponseAssembler;
import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.dto.*;
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
    public EntityModel<ReservationResponseDto> checkInForReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id : " + reservationId + ", is not found"));
        if (!reservation.getCheckinDate().equals(LocalDate.now())) {
            reservation.setCheckinDate(LocalDate.now());
        }
        reservation.setReservationStatusEnum(ReservationStatusEnum.ONGOING);

        reservationRepository.save(reservation);

        return reservationResponseAssembler.toModel(reservationRepository.save(reservation));
    }

    @Override
    public EntityModel<ReservationResponseDto> checkoutForReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id : " + reservationId + ", is not found"));

        // Update reservation status to FINISHED
        reservation.setReservationStatusEnum(ReservationStatusEnum.FINISHED);

        // Set associated rooms to be available
        setReservedRoomsToAvailable(reservation.getReservationRooms());

        // Save the updated reservation
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Return the updated reservation wrapped in an EntityModel
        return reservationResponseAssembler.toModel(updatedReservation);
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
    public CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations(Long id, String name, LocalDate time) {
        SecurityUtils.checkIfAdminAuthority();
        List<EntityModel<ReservationResponseDto>> reservations = reservationRepository.findWithIdNameDate(id, name, time).stream().map(reservationResponseAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(reservations);
    }
    @Override
    public Page<ReservationResponseDto> getAllReservations(Long userId, String name, LocalDate checkinDate, LocalDate checkoutDate, Pageable pageable) {
        // if (userId != null) {
        //     return reservationRepository.findByUserEntityId(userId, pageable).map(this::toReservationResponseDto);
        // } else if (checkinDate != null && checkoutDate != null) {
        //     return reservationRepository.findByCheckinDateAndCheckoutDate(checkinDate, checkoutDate, pageable).map(this::toReservationResponseDto);
        // } else {
        //     return reservationRepository.findAll(pageable).map(this::toReservationResponseDto);
        // }
        return reservationRepository.findWithIdNameDate(userId, name, checkinDate, checkoutDate, pageable).map(this::toReservationResponseDto);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            if ((reservation.getCheckinDate().isBefore(checkinDate) && reservation.getCheckoutDate().isAfter(checkinDate) ) || (reservation.getCheckinDate().isBefore(checkoutDate) && reservation.getCheckoutDate().isAfter(checkoutDate))){
                for (ReservationRoom reservationRoom : reservation.getReservationRooms()) {
                    if (reservationRoom.getRoom().getId().equals(roomId)) {
                        return false;
                    }
                }
            }
        }
        return true;
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
    public EntityModel<ReservationResponseDto> bookRoom(ReservationRequestDto reservationRequestDto) throws BadRequestException{
        Reservation reservation = new Reservation();
        reservation.setCheckinDate(reservationRequestDto.getCheckinDate());
        reservation.setCheckoutDate(reservationRequestDto.getCheckoutDate());
        reservation.setNumAdults(reservationRequestDto.getNumAdults());
        reservation.setNumChildren(reservationRequestDto.getNumChildren());
        UserEntity user = userRepository.findById(reservationRequestDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        reservation.setUserEntity(user);
        reservation.getPaymentAmount();
        List<Long> resRoom = reservationRequestDto.getRoomIds();
        Room room = roomRepository.findById(resRoom.get(0)).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if (!isRoomAvailable(room.getId(), reservation.getCheckinDate(), reservation.getCheckoutDate())) {
            throw new BadRequestException("Room is not available for the selected dates");
        }
        reservation.getReservationRooms().add(new ReservationRoom(reservation, room));
        reservation.setReservationStatusEnum(ReservationStatusEnum.ONHOLD);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationResponseAssembler.toModel(savedReservation);
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
        // checkRoomsAvailability(reservationRequestDto.getRoomIds());
        LocalDate checkinDate = reservationRequestDto.getCheckinDate();
        LocalDate checkoutDate = reservationRequestDto.getCheckoutDate();
        for (Long roomId : reservationRequestDto.getRoomIds()) {
            if (!isRoomAvailable(roomId, checkinDate, checkoutDate)) {
                throw new BadRequestException("Room is not available for the selected dates");
            }
        }
        Reservation reservation = fromRequestToEntity(reservationRequestDto, user);
        EntityModel<ReservationResponseDto> model = reservationResponseAssembler.toModel(reservation);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).addReservation(reservationRequestDto)).withRel("makeReservation"));
        return model;
    }

    @Override
    public EntityModel<ReservationResponseDto> createReservation(ReservationRequestDto reservationRequestDto) throws BadRequestException {
        UserEntity user = userRepository.findById(reservationRequestDto.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        LocalDate checkinDate = reservationRequestDto.getCheckinDate();
        LocalDate checkoutDate = reservationRequestDto.getCheckoutDate();
        for (Long roomId : reservationRequestDto.getRoomIds()) {
            if (!isRoomAvailable(roomId, checkinDate, checkoutDate)) {
                throw new BadRequestException("Room is not available for the selected dates");
            }
        }
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

        List<ReservationRoom> reservationRooms = reservationRequestDto.getRoomIds().stream().map(roomId -> {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
            return new ReservationRoom(reservation, room);
        }).collect(Collectors.toList());

        reservation.setReservationRooms(reservationRooms);

        // Proceed with reservation
        // reserveRoomsIfAvailable(reservationRequestDto.getRoomIds());
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
    public EntityModel<ReservationResponseDto> addRoomToReservation(Long id, Long roomId) throws BadRequestException {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if(!isRoomAvailable(roomId, reservation.getCheckinDate(), reservation.getCheckoutDate())){
            throw new BadRequestException("Room is not available for the selected dates");
        }
        reservation.getReservationRooms().add(new ReservationRoom(reservation, room));
        Reservation updatedReservation = reservationRepository.save(reservation);
        return reservationResponseAssembler.toModel(updatedReservation);
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
        List<Reservation> upcomingReservations = reservationRepository.findByUserEntityAndCheckinDateAfter(user, LocalDate.now());
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


    public Page<ReservationResponseDto> getAllReservations(Long userId, LocalDate checkinDate, LocalDate checkoutDate, Pageable pageable) {
        if (userId != null) {
            return reservationRepository.findByUserEntityId(userId, pageable).map(this::toReservationResponseDto);
        } else if (checkinDate != null && checkoutDate != null) {
            return reservationRepository.findByCheckinDateAndCheckoutDate(checkinDate, checkoutDate, pageable).map(this::toReservationResponseDto);
        } else {
            return reservationRepository.findAll(pageable).map(this::toReservationResponseDto);
        }
    }

    public Page<ReservationResponseDto> getReservationsByDate(LocalDate date, Pageable pageable) {
        return reservationRepository.findByCheckinDate(date, pageable).map(this::toReservationResponseDto);
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

    private ReservationResponseDto toReservationResponseDto(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate(),
                reservation.getNumAdults(),
                reservation.getNumChildren(),
                reservation.getPaymentAmount(),
                reservation.getUserEntity().getId(),
                mapReservationToReservationPaymentDto(reservation),
                reservation.getReservationStatusEnum()
        );
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
