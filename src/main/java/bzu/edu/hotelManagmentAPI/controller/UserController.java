package bzu.edu.hotelManagmentAPI.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @GetMapping("")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<String> registerUser() {
        return ResponseEntity.ok("Message");
    }

}
