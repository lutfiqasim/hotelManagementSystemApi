package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.dto.AuthResponseDto;
import bzu.edu.hotelManagmentAPI.dto.LoginDto;
import bzu.edu.hotelManagmentAPI.dto.RegisterDto;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthenticationService authService;

    @Autowired
    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login into the system",
            description = "This API allows a new User to register",
            responses = {
                    @ApiResponse(description = "User logged in successfully", responseCode = "200", content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(description = "User email or password is incorrect", responseCode = "401"),
                    @ApiResponse(description = "Invalid input(bad request)", responseCode = "400")
            })
    @PostMapping("/login")
    public ResponseEntity<EntityModel<AuthResponseDto>> login(@Valid @RequestBody LoginDto loginDto) {
        EntityModel<AuthResponseDto> response = authService.authenticateUser(loginDto);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Operation(summary = "Register a new User",
            description = "This API allows a new User to register",
            responses = {
                    @ApiResponse(description = "User registered successfully", responseCode = "200", content = @Content(schema = @Schema(implementation = UserEntity.class))),
                    @ApiResponse(description = "User email/phone number already exists", responseCode = "409"),
                    @ApiResponse(description = "Invalid input(bad request)", responseCode = "400")
            })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        String result = authService.registerUser(registerDto);
        if ("User registered successfully".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }
}
