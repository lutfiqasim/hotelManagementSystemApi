package bzu.edu.hotelManagmentAPI.assembler;

import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.controller.UserController;
import bzu.edu.hotelManagmentAPI.dto.AuthResponseDto;
import bzu.edu.hotelManagmentAPI.dto.ReservationResponseDto;
import bzu.edu.hotelManagmentAPI.model.Reservation;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class ReservationResponseAssembler implements RepresentationModelAssembler<Reservation, EntityModel<ReservationResponseDto>> {
    @Override
    public EntityModel<ReservationResponseDto> toModel(Reservation entity) {
        ReservationResponseDto responseDto = new ReservationResponseDto();
        EntityModel<ReservationResponseDto> entityModel = EntityModel.of(responseDto);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(entity.getUserEntity().getId())).withRel("User"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).getUserReservations(entity.getUserEntity().getId())).withSelfRel());
//        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).g))
        return entityModel;
    }
}
