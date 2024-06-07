package bzu.edu.hotelManagmentAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email address")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "password should at least be 6 chars")
    private String password;

    @NotBlank(message = "First name is required")
    @JsonProperty("first_name")
    private String firstName;

    @NotBlank(message = "last name is required")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number")
    @JsonProperty("phone_no")
    private String phoneNo;
}
