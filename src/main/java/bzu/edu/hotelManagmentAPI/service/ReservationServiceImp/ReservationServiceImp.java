package bzu.edu.hotelManagmentAPI.service.ReservationServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.ReservationResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationRequestDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationUpdateDto;
import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.Reservation;
import bzu.edu.hotelManagmentAPI.model.Room;
import bzu.edu.hotelManagmentAPI.model.RoomStatus;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.repository.ReservationRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomRepository;
import bzu.edu.hotelManagmentAPI.repository.RoomStatusRepository;
import bzu.edu.hotelManagmentAPI.repository.UserRepository;
import bzu.edu.hotelManagmentAPI.security.SecurityUtils;
import bzu.edu.hotelManagmentAPI.service.ReservationService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class ReservationServiceImp implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomStatusRepository roomStatusRepository;
    private final ReservationResponseAssembler reservationResponseAssembler;

    @Autowired
    public ReservationServiceImp(ReservationRepository reservationRepository, UserRepository userRepository, RoomRepository roomRepository, RoomStatusRepository roomStatusRepository, ReservationResponseAssembler reservationResponseAssembler) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.reservationResponseAssembler = reservationResponseAssembler;
        this.roomRepository = roomRepository;
        this.roomStatusRepository = roomStatusRepository;
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
    public CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations() {
        SecurityUtils.checkIfAdminAuthority();
        throw new UnsupportedOperationException("Request not supported yet, contact support for more");
    }

    @Override
    public void payForReservation(ReservationPaymentDto reservationPaymentDto) {
        Reservation reservation = reservationRepository.findById(reservationPaymentDto.getReservationId()).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        reservation.setPaymentStatus("Paid");
        reservationRepository.save(reservation);
    }
    
    public Page<EntityModel<ReservationResponseDto>> getAllReservations(int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<EntityModel<ReservationResponseDto>> reservations = reservationRepository.findAll(pageable).map(reservationResponseAssembler::toModel);
        return reservations;
    }

    // @Override
    // public ResponseEntity<?> getResevationAtDay


    @Override
    public EntityModel<ReservationResponseDto> getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        SecurityUtils.checkIfSameUserOrAdmin(reservation.getUserEntity());
        return reservationResponseAssembler.toModel(reservation);
    }

    @Override
    public EntityModel<ReservationResponseDto> createReservation(@Valid ReservationRequestDto reservationRequestDto) throws BadRequestException {
        UserEntity user = userRepository.findById(reservationRequestDto.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        reserveRoomsIfAvailable(reservationRequestDto.getRoomIds());
        Reservation reservation = fromRequestToEntity(reservationRequestDto, user);
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
    public ResponseEntity<CollectionModel<EntityModel<ReservationResponseDto>>> getUserReservationsOnHold(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityUtils.checkIfSameUserOrAdmin(user);
        List<Reservation> onHoldResrevations = reservationRepository.findByUserEntityAndPaymentStatus(user, "OnHold");
        if (onHoldResrevations.isEmpty()) {
            return new ResponseEntity<>(CollectionModel.empty(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservationResponseAssembler.toCollectionModel(onHoldResrevations), HttpStatus.OK);
//        throw new UnsupportedOperationException("Request not supported yet, contact support for more");

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

    private void reserveRoomsIfAvailable(List<Long> roomIds) throws BadRequestException {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        RoomStatus roomStatus = roomStatusRepository.findByStatusName("reserved").orElseThrow(() -> new EnumConstantNotPresentException(RoomStatusEnum.class, RoomStatusEnum.Reserved.name()));
        for (Room room : rooms) {
            if (room.getStatus().getStatusName().equals(RoomStatusEnum.Reserved.name())) {
                throw new BadRequestException("Requested Room with id = " + room.getId() + ", Room number = " + room.getRoomNumber() + ", is already reserved");
            }
            room.setStatus(roomStatus);
        }
        roomRepository.saveAll(rooms);
    }
}
