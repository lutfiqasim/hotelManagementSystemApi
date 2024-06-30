package bzu.edu.hotelManagmentAPI.assembler;

import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.controller.ReservationControllerV2;
import bzu.edu.hotelManagmentAPI.controller.UserController;
import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.enums.PaymentStatus;
import bzu.edu.hotelManagmentAPI.model.Reservation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class ReservationResponseAssembler implements RepresentationModelAssembler<Reservation, EntityModel<ReservationResponseDto>> {

    @Override
    public EntityModel<ReservationResponseDto> toModel(Reservation entity) {
        EntityModel<ReservationResponseDto> entityModel = getModel(entity);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(entity.getUserEntity().getId())).withRel("User"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getUserReservations(entity.getUserEntity().getId())).withSelfRel());
        if (entity.getPayment() != null && entity.getPayment().getPaymentStatus().equals(PaymentStatus.OnHold)) {
            entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).payForReservation(entity.getId())).withRel("payForReservation"));
        }
        return entityModel;
    }

    private EntityModel<ReservationResponseDto> getModel(Reservation entity) {
        ReservationResponseDto dto = new ReservationResponseDto(
                entity.getId(),
                entity.getCheckinDate(),
                entity.getCheckoutDate(),
                entity.getNumAdults(),
                entity.getNumChildren(),
                entity.getUserEntity().getId(),
                entity.getReservationRooms().stream().map(reservationRoom -> reservationRoom.getRoom().getId()).toList());
        if (entity.getPayment() != null) {
            ReservationPaymentDto paymentDto = new ReservationPaymentDto();
            paymentDto.setId(entity.getPayment().getId());
            paymentDto.setPaymentMethod(entity.getPayment().getPaymentMethod().name());
            paymentDto.setPaymentStatus(entity.getPayment().getPaymentStatus().name());
            if (entity.getReservationRooms() != null) {
                dto.setPaymentAmount(entity.getPaymentAmount());
                paymentDto.setAmount(entity.getPaymentAmount());
            }
            dto.setReservationStatus(entity.getReservationStatusEnum().name());
            dto.setPayment(paymentDto);
//          paymentDto.setPaymentDate(entity.getPayment());
        }
        return EntityModel.of(dto);
    }
}
