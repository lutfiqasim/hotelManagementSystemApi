package bzu.edu.hotelManagmentAPI.assembler;

import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.controller.ReservationControllerV2;
import bzu.edu.hotelManagmentAPI.controller.UserController;
import bzu.edu.hotelManagmentAPI.dto.ReservationPaymentDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
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
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationControllerV2.class).getUserReservations(entity.getUserEntity().getId())).withSelfRel());
//        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).g))
        return entityModel;
    }

    private EntityModel<ReservationResponseDto> getModel(Reservation entity) {
        ReservationResponseDto dto = new ReservationResponseDto(
                entity.getId(),
                entity.getCheckinDate(),
                entity.getCheckoutDate(),
                entity.getNumAdults(),
                entity.getNumChildren(),
                entity.getPaymentAmount(),
                entity.getUserEntity().getId());
                if(entity.getPayment() != null) {
                    ReservationPaymentDto paymentDto = new ReservationPaymentDto();
                    paymentDto.setPaymentMethod(entity.getPayment().getPaymentMethod().name());
                    paymentDto.setPaymentStatus(entity.getPayment().getPaymentStatus().name());
                    paymentDto.setAmount(entity.getPayment().getAmount());
                    paymentDto.setId(entity.getPayment().getId());
                    dto.setPayment(paymentDto);
                }
        return EntityModel.of(dto);
    }
}
