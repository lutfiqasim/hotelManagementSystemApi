package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.assembler.UserResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.UserEntityResponse;
import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.repository.UserRepository;
import bzu.edu.hotelManagmentAPI.security.HasAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserResponseAssembler userResponseAssembler;

    @Autowired
    public UserController(UserRepository userRepository, UserResponseAssembler userResponseAssembler) {
        this.userRepository = userRepository;
        this.userResponseAssembler = userResponseAssembler;
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admins")
    public ResponseEntity<CollectionModel<EntityModel<UserEntityResponse>>> getAllAdmins() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && HasAuthority.hasAuthority(auth, UserRole.ADMIN.name())) {
            List<UserEntity> users = userRepository.findByRole("ADMIN");
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<EntityModel<UserEntityResponse>> userModels = users.stream()
                    .map(userResponseAssembler::toModel)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(CollectionModel.of(userModels));
        }

        throw new AuthorizationServiceException("User Unauthorized");

    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserEntityResponse>> getUserById(@PathVariable Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(userResponseAssembler.toModel(user));
    }

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<UserEntityResponse>>> getAllCustomers() {
        List<UserEntity> customers = userRepository.findByRole("CUSTOMER");
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        CollectionModel<EntityModel<UserEntityResponse>> collectionModel = userResponseAssembler.toCollectionModel(customers);
        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

}
