package bzu.edu.hotelManagmentAPI.assembler;

import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.controller.UserController;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.model.Reservation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class ReservationResponseAssembler implements RepresentationModelAssembler<Reservation, EntityModel<ReservationResponseDto>> {
    @Override
    public EntityModel<ReservationResponseDto> toModel(Reservation entity) {
        EntityModel<ReservationResponseDto> entityModel = getModel(entity);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(entity.getUserEntity().getId())).withRel("User"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getUserReservations(entity.getUserEntity().getId())).withSelfRel());
        // TODO:Add reserved rooms
//        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn()))
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
                entity.getPaymentStatus(),
                entity.getPaymentAmount(),
                entity.getUserEntity().getId());
        return EntityModel.of(dto);
    }
}
