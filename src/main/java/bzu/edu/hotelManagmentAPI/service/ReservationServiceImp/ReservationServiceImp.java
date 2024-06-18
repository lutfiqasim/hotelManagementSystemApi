package bzu.edu.hotelManagmentAPI.service.ReservationServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.ReservationResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationUpdateDto;
import bzu.edu.hotelManagmentAPI.enums.PaymentMethod;
import bzu.edu.hotelManagmentAPI.enums.PaymentStatus;
import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.Payment;
import bzu.edu.hotelManagmentAPI.model.Reservation;
import bzu.edu.hotelManagmentAPI.model.Room;
import bzu.edu.hotelManagmentAPI.model.RoomStatus;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.repository.PaymentRepository;
import bzu.edu.hotelManagmentAPI.repository.ReservationRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomStatusRepository;
import bzu.edu.hotelManagmentAPI.repository.UserRepository;
import bzu.edu.hotelManagmentAPI.security.SecurityUtils;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

    @Autowired
    public ReservationServiceImp(ReservationRepository reservationRepository, UserRepository userRepository, RoomRepository roomRepository, RoomStatusRepository roomStatusRepository, ReservationResponseAssembler reservationResponseAssembler, PaymentRepository paymentRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.reservationResponseAssembler = reservationResponseAssembler;
        this.roomRepository = roomRepository;
        this.roomStatusRepository = roomStatusRepository;
        this.paymentRepository = paymentRepository;

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
    public Page<EntityModel<ReservationResponseDto>> getAllReservations(Integer page, Integer size, Long id, String name, LocalDate time){
        SecurityUtils.checkIfAdminAuthority();
        return reservationRepository.findWithIdNameDate(id, name, time, Pageable.ofSize(size).withPage(page)).map(reservationResponseAssembler::toModel);
    }

    @Override
    public CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations(Long id, String name, LocalDate time){
        SecurityUtils.checkIfAdminAuthority();
        List<EntityModel<ReservationResponseDto>> reservations = reservationRepository.findWithIdNameDate(id, name, time).stream().map(reservationResponseAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(reservations);
    }

    @Override
    public void payForReservation(ReservationPaymentDto reservationPaymentDto) {
        Reservation reservation = reservationRepository.findById(reservationPaymentDto.getReservationId()).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        reservation.getPayment().setPaymentStatus(PaymentStatus.Paid);
        reservationRepository.save(reservation);
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

        return reservationResponseAssembler.toModel(reservation); //add confirm reservation link here
    }
    
    @Override
    public EntityModel<ReservationResponseDto> createReservation(@Valid ReservationRequestDto reservationRequestDto) throws BadRequestException {
        UserEntity user = userRepository.findById(reservationRequestDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        reserveRoomsIfAvailable(reservationRequestDto.getRoomIds());
        Reservation reservation = fromRequestToEntity(reservationRequestDto, user);
        Payment payment = new Payment();
        ReservationPaymentDto paymentDto = reservationRequestDto.getPayment();
        try {
            PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentDto.getPaymentStatus());
            payment.setPaymentStatus(paymentStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid Payment Status");
        }
        try {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentDto.getPaymentMethod());
            payment.setPaymentMethod(paymentMethod);
        }
        catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid Payment Method");
        }
        payment.setAmount(reservation.getPaymentAmount());
        payment.setReservation(reservation);
        reservation.setPayment(payment);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationResponseAssembler.toModel(savedReservation);
    }

    @Override
    public EntityModel<ReservationResponseDto> updateReservation(Long id, ReservationUpdateDto reservationUpdateDto) {
        throw new UnsupportedOperationException("Request not supported yet, contact support for more");
    }

    @Override
    public void deleteReservation(Long id) {
        throw new UnsupportedOperationException("Request not supported yet, contact support for more");
    }

    @Override
    public CollectionModel<EntityModel<ReservationResponseDto>> getUserReservationsOnHold(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityUtils.checkIfSameUserOrAdmin(user);
        List<Reservation> onHoldReservations = reservationRepository.findByUserEntityAndPaymentStatus(user, "OnHold");
        if (onHoldReservations.isEmpty()) {
            return CollectionModel.empty();
        }
        return reservationResponseAssembler.toCollectionModel(onHoldReservations);
    }

    @Override
    public CollectionModel<EntityModel<ReservationResponseDto>> getUpcomingReservations(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityUtils.checkIfSameUserOrAdmin(user);
        List<Reservation> upcomingReservations = reservationRepository.findByUserEntityIdAndCheckinDateAfter(user, LocalDate.now());
        if (upcomingReservations.isEmpty()) {
            return CollectionModel.empty();
        }
        return reservationResponseAssembler.toCollectionModel(upcomingReservations);
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
            if (room.getStatus().getStatusName().name().equalsIgnoreCase(RoomStatusEnum.Reserved.name())) {
                throw new BadRequestException("Requested Room with id = " + room.getId() + ", Room number = " + room.getRoomNumber() + ", is already reserved");
            }
        }
    }

    private void reserveRoomsIfAvailable(List<Long> roomIds) throws BadRequestException {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        checkRoomsAvailability(roomIds);
        RoomStatus roomStatus = roomStatusRepository.findByStatusName(RoomStatusEnum.Reserved).orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Reserved.name()));
        for (Room room : rooms) {
            room.setStatus(roomStatus);
        }
        roomRepository.saveAll(rooms);
    }

    // private void checkIfSameUserOrAdmin(UserEntity user) {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     if (!auth.getName().equals(user.getEmailAddress())) {
    //         checkIfAdminAuthority();
    //     }
    // }

    // private static void checkIfAdminAuthority() {
    //     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     if (auth == null || !HasAuthority.hasAuthority(auth, UserRole.ADMIN.name())) {
    //         throw new AuthorizationServiceException("User Unauthorized");
    //     }
    // }

    // @Override
    // public CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations(Integer id, String name, LocalDate time) {
    //     CollectionModel<EntityModel<ReservationResponseDto>> reservations = reservationRepository.findWithIdNameDate(id, name, time).stream().map(reservationResponseAssembler::toModel).collect(Collectors.toList()).stream().collect(Collectors.collectingAndThen(Collectors.toList(), CollectionModel::of));
    //     return reservations;
    // }
}
