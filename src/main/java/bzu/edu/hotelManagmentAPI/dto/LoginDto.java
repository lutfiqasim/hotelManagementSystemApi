package bzu.edu.hotelManagmentAPI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @Email
    @NotBlank(message = "email can't be empty")
    private String email;

    @NotBlank(message = "password can't be empty")
    private String password;
}
