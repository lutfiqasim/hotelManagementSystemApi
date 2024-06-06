package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.AuthResponseDto;
import bzu.edu.hotelManagmentAPI.dto.LoginDto;
import bzu.edu.hotelManagmentAPI.dto.RegisterDto;
import org.springframework.hateoas.EntityModel;

public interface AuthenticationService {

    /**
     * Authenticates a user based on login credentials.
     *
     * @param loginDto the login DTO containing user credentials
     * @return an entity model containing the authentication response
     */
    public abstract EntityModel<AuthResponseDto> authenticateUser(LoginDto loginDto);

    /**
     * Registers a new user based on registration details.
     *
     * @param registerDto the registration DTO containing user details
     * @return a string message indicating the result of the registration
     */
    public abstract String registerUser(RegisterDto registerDto);
}
