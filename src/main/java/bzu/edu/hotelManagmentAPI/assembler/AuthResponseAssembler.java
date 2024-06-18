package bzu.edu.hotelManagmentAPI.assembler;

import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.controller.ReservationControllerV2;
import bzu.edu.hotelManagmentAPI.controller.UserController;
import bzu.edu.hotelManagmentAPI.dto.AuthResponseDto;
import bzu.edu.hotelManagmentAPI.model.UserEntity;

import java.time.LocalDate;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseAssembler implements RepresentationModelAssembler<UserEntity, EntityModel<AuthResponseDto>> {
    @Override
    public EntityModel<AuthResponseDto> toModel(UserEntity user) {
        AuthResponseDto responseDto = new AuthResponseDto("");
        EntityModel<AuthResponseDto> entityModel = EntityModel.of(responseDto);
        if (user != null) {
            for (var role : user.getRoles()) {
                if ("ADMIN".equals(role.getName().name())) {
                    entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllAdmins()).withRel("admins"));
//                    entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationControllerV2.class).getAllReservations( 0, 50, LocalDate.now(),"Belal", 1L)).withRel("reservations"));
                }
                else { //customer
                    // entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn

                }
                entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
                //add reservations link available rooms etc....
            }
        }
        return entityModel;

    }
}
