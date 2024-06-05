package bzu.edu.hotelManagmentAPI.dto;


import bzu.edu.hotelManagmentAPI.model.UserEntity;
import lombok.Data;
import bzu.edu.hotelManagmentAPI.model.Role;

import java.util.List;

@Data
public class UserEntityResponse {
    private Long id;
    private String emailAddress;
    private String firstName;
    private String lastName;
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

