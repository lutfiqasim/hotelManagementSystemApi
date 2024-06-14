package bzu.edu.hotelManagmentAPI.assembler;

import bzu.edu.hotelManagmentAPI.controller.ReservationController;
import bzu.edu.hotelManagmentAPI.enums.RoomStatusEnum;
import bzu.edu.hotelManagmentAPI.model.RoomStatus;
import org.apache.coyote.BadRequestException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import bzu.edu.hotelManagmentAPI.controller.RoomController;
import bzu.edu.hotelManagmentAPI.dto.RoomResponseDto;
import bzu.edu.hotelManagmentAPI.model.Room;

@Component
public class RoomResponseAssembler implements RepresentationModelAssembler<Room, EntityModel<RoomResponseDto>> {
    @Override
    public EntityModel<RoomResponseDto> toModel(Room entity) {
        RoomResponseDto userEntityResponse = new RoomResponseDto(entity);
        EntityModel<RoomResponseDto> entityModel = EntityModel.of(userEntityResponse);
        //self link for now
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoomController.class)
                .getRoomById(entity.getId())).withSelfRel());
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RoomController.class).getAllRooms(1, 10)).withRel("rooms"));
        if (entity.getStatus().getStatusName().name().equals(RoomStatusEnum.Available.name())) {
            try {
                entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReservationController.class).addReservation(null)).withRel("reserveRoom").expand(entity.getId()));
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
        return entityModel;
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> toCollectionModel(Iterable<? extends Room> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

}
