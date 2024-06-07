package bzu.edu.hotelManagmentAPI.assembler;

import bzu.edu.hotelManagmentAPI.controller.UserController;
import bzu.edu.hotelManagmentAPI.dto.UserEntityResponse;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserResponseAssembler implements RepresentationModelAssembler<UserEntity, EntityModel<UserEntityResponse>> {
    @Override
    public EntityModel<UserEntityResponse> toModel(UserEntity entity) {
        UserEntityResponse userEntityResponse = new UserEntityResponse(entity);
        EntityModel<UserEntityResponse> entityModel = EntityModel.of(userEntityResponse);
        //self link for now
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserById(entity.getId())).withSelfRel());

        return entityModel; 
    }

    @Override
    public CollectionModel<EntityModel<UserEntityResponse>> toCollectionModel(Iterable<? extends UserEntity> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
