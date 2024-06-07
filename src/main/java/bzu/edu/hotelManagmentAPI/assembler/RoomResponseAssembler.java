package bzu.edu.hotelManagmentAPI.assembler;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

        return entityModel; 
    }

    @Override
    public CollectionModel<EntityModel<RoomResponseDto>> toCollectionModel(Iterable<? extends Room> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

}
