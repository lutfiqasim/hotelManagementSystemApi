package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.assembler.UserResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.UserEntityResponse;
import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/admins")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CollectionModel<EntityModel<UserEntityResponse>>> getAllAdmins() {
        List<UserEntity> users = userRepository.findByRole("ADMIN");
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No registered Employees where found");
        }
        List<EntityModel<UserEntityResponse>> userModels = users.stream()
                .map(userResponseAssembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(userModels));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserEntityResponse>> getUserById(@PathVariable Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok(userResponseAssembler.toModel(user));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> registerUser() {
        return ResponseEntity.ok("Message");
    }

}
