// package bzu.edu.hotelManagmentAPI.service.ReservationServiceImp;

// import bzu.edu.hotelManagmentAPI.assembler.ReservationResponseAssembler;
// import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
// import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
// import bzu.edu.hotelManagmentAPI.model.Reservation;
// import bzu.edu.hotelManagmentAPI.repository.ReservationRepository;
// import bzu.edu.hotelManagmentAPI.service.ReservationService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.hateoas.CollectionModel;
// import org.springframework.hateoas.EntityModel;
// import org.springframework.stereotype.Service;

// import java.util.List;
// @Service
// public class ReservationServiceImp implements ReservationService {
//     private final ReservationRepository reservationRepository;
//     private final ReservationResponseAssembler reservationResponseAssembler;

//     @Autowired
//     public ReservationServiceImp(ReservationRepository reservationRepository, ReservationResponseAssembler reservationResponseAssembler) {
//         this.reservationRepository = reservationRepository;
//         this.reservationResponseAssembler = reservationResponseAssembler;
//     }

//     @Override
//     public CollectionModel<EntityModel<ReservationResponseDto>> getUserReservations(Long userId) {

//         List<Reservation> reservationList = reservationRepository.findByUserId(userId);
//         if (reservationList.isEmpty()) {
//             return CollectionModel.empty();
//         }
//         return reservationResponseAssembler.toCollectionModel(reservationList);
//     }

//     @Override
//     public CollectionModel<EntityModel<ReservationResponseDto>> getAllReservations() {
//         return null;
//     }

//     @Override
//     public String payForReservation(ReservationPaymentDto reservationPaymentDto) {
//         return null;
//     }

//     @Override
//     public EntityModel<ReservationResponseDto> getReservationById(Long id) {
//         return null;
//     }

//     @Override
//     public EntityModel<ReservationResponseDto> createReservation(Reservation reservation) {
//         return null;
//     }

//     @Override
//     public EntityModel<ReservationResponseDto> updateReservation(Long id, Reservation reservation) {
//         return null;
//     }

//     @Override
//     public void deleteReservation(Long id) {

//     }

//     @Override
//     public CollectionModel<EntityModel<ReservationResponseDto>> getUserReservationsOnHold(Long userId) {
//         return null;
//     }

//     @Override
//     public CollectionModel<EntityModel<ReservationResponseDto>> getUpcomingReservations(Long userId) {
//         return null;
//     }
// }
