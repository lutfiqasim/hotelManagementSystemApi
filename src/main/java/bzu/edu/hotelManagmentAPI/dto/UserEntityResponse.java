package bzu.edu.hotelManagmentAPI.dto;


import bzu.edu.hotelManagmentAPI.model.UserEntity;
import lombok.Data;
import bzu.edu.hotelManagmentAPI.model.Role;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UserEntityResponse {
    private Long id;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;
    
    private List<Role> roles;

    public UserEntityResponse(UserEntity user) {
        this.id = user.getId();
        this.emailAddress = user.getEmailAddress();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.roles = user.getRoles();
    }
}

