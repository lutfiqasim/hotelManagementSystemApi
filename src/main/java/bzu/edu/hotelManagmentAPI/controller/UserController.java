package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.UserEntityResponse;
import bzu.edu.hotelManagmentAPI.dto.UserPatchDto;
import bzu.edu.hotelManagmentAPI.dto.UserUpdateDto;
import bzu.edu.hotelManagmentAPI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admins")
    public ResponseEntity<CollectionModel<EntityModel<UserEntityResponse>>> getAllAdmins() {
        CollectionModel<EntityModel<UserEntityResponse>> response = userService.getAllAdmins();
        if (response != null && !response.getContent().isEmpty()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserEntityResponse>> getUserById(@PathVariable Long id) {
        EntityModel<UserEntityResponse> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<UserEntityResponse>>> getAllCustomers() {
        CollectionModel<EntityModel<UserEntityResponse>> response = userService.getAllCustomers();
        if (response != null && !response.getContent().isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserEntityResponse>>> getAllUsers() {
        CollectionModel<EntityModel<UserEntityResponse>> response = userService.getAllUsers();
        if (response != null && !response.getContent().isEmpty()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserEntityResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        EntityModel<UserEntityResponse> response = userService.updateUser(id, userUpdateDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<UserEntityResponse>> patchUser(@PathVariable Long id, @Valid @RequestBody UserPatchDto userPatchDto) {
        EntityModel<UserEntityResponse> response = userService.patchUser(id, userPatchDto);
        return ResponseEntity.ok(response);
    }

}