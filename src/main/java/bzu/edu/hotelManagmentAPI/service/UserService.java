package bzu.edu.hotelManagmentAPI.service;

import bzu.edu.hotelManagmentAPI.dto.UserEntityResponse;
import bzu.edu.hotelManagmentAPI.dto.UserPatchDto;
import bzu.edu.hotelManagmentAPI.dto.UserUpdateDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface UserService {
    /**
     * Fetches all admin users.
     *
     * @return a collection model containing the admin users
     */
    public abstract CollectionModel<EntityModel<UserEntityResponse>> getAllAdmins();

    /**
     * Fetches a user by their ID.
     *
     * @param id the ID of the user to fetch
     * @return an entity model containing the user details
     */
    public abstract EntityModel<UserEntityResponse> getUserById(Long id);
    
    /**
     * Fetches a user by their email.
     * @param email the email of the user to fetch
     * @return an entity model containing the user details
     */
    public abstract EntityModel<UserEntityResponse> getUserByEmail(String email);

    /**
     * Fetches all customer users.
     *
     * @return a collection model containing the customer users
     */
    public abstract CollectionModel<EntityModel<UserEntityResponse>> getAllCustomers();

    /**
     * Fetches all users
     *
     * @return a collection model containing All users customers and admins
     */
    public abstract CollectionModel<EntityModel<UserEntityResponse>> getAllUsers();

    /**
     * Update User by his id
     *
     * @param id            the id of the user to update
     * @param userUpdateDto the new User entity to be updated to
     * @return an entity model containing new user details
     */
    public abstract EntityModel<UserEntityResponse> updateUser(Long id, UserUpdateDto userUpdateDto);

    public abstract EntityModel<UserEntityResponse> patchUser(Long id, UserPatchDto userPatchDto);

    /**
     * Delete User by his id
     *
     * @param id
     */
    public abstract void deleteUser(Long id);


}
