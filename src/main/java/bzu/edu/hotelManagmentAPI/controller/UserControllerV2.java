package bzu.edu.hotelManagmentAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bzu.edu.hotelManagmentAPI.dto.UserEntityResponse;
import bzu.edu.hotelManagmentAPI.service.UserService;

@RestController
@RequestMapping("/api/v2/users")
public class UserControllerV2 {


    protected final UserService userService;

    @Autowired
    public UserControllerV2(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserEntityResponse>>> getAllUsers(@RequestParam(required = false) String role) {
        if (role != null && role.equalsIgnoreCase("admin")) {
            return ResponseEntity.ok(userService.getAllAdmins());
        }
        if (role != null && role.equalsIgnoreCase("customer")) {
            return ResponseEntity.ok(userService.getAllCustomers());
        }
        CollectionModel<EntityModel<UserEntityResponse>> response = userService.getAllUsers();
        if (response != null && !response.getContent().isEmpty()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.noContent().build();
    }

}
