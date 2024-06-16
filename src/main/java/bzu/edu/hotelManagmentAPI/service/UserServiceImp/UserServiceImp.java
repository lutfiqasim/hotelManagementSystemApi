package bzu.edu.hotelManagmentAPI.service.UserServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.UserResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.UserEntityResponse;
import bzu.edu.hotelManagmentAPI.dto.UserPatchDto;
import bzu.edu.hotelManagmentAPI.dto.UserUpdateDto;
import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.exception.ResourceNotFoundException;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.repository.UserRepository;
import bzu.edu.hotelManagmentAPI.security.HasAuthority;
import bzu.edu.hotelManagmentAPI.security.SecurityUtils;
import bzu.edu.hotelManagmentAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserResponseAssembler userResponseAssembler;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImp(UserRepository userRepository, UserResponseAssembler userResponseAssembler, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userResponseAssembler = userResponseAssembler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CollectionModel<EntityModel<UserEntityResponse>> getAllAdmins() {
//        checkIfAdminAuthority();
        List<UserEntity> users = userRepository.findByRole(UserRole.ADMIN);
        if (users.isEmpty()) {
            return CollectionModel.empty();
        }
        List<EntityModel<UserEntityResponse>> userModels = users.stream()
                .map(userResponseAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(userModels);
    }

    @Override
    public EntityModel<UserEntityResponse> getUserById(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        SecurityUtils.checkIfSameUserOrAdmin(user);
        return userResponseAssembler.toModel(user);
    }

    @Override
    public EntityModel<UserEntityResponse> getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmailAddress(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (HasAuthority.hasAuthority(auth, UserRole.ADMIN.name()) || auth.getName().equals(user.getEmailAddress()))) {
            return userResponseAssembler.toModel(user);
        }
        throw new AuthorizationServiceException("User unAuthorized");
    }

    @Override
    public CollectionModel<EntityModel<UserEntityResponse>> getAllCustomers() {
        List<UserEntity> customers = userRepository.findByRole(UserRole.CUSTOMER);
        if (customers.isEmpty()) {
            return CollectionModel.empty();
        }
        return userResponseAssembler.toCollectionModel(customers);
    }

    @Override
    public CollectionModel<EntityModel<UserEntityResponse>> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            return CollectionModel.empty();
        }
        return userResponseAssembler.toCollectionModel(users);
    }

    @Override
    public EntityModel<UserEntityResponse> updateUser(Long id, UserUpdateDto userUpdateDto) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserEntity loggedInUser = userRepository.findByEmailAddress(auth.getName())
//                .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found"));

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SecurityUtils.checkIfSameUserOrAdmin(user);


        if (StringUtils.hasText(userUpdateDto.getFirstName())) {
            user.setFirstName(userUpdateDto.getFirstName());
        }

        if (StringUtils.hasText(userUpdateDto.getLastName())) {
            user.setLastName(userUpdateDto.getLastName());
        }

        if (StringUtils.hasText(userUpdateDto.getPhoneNumber())) {
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }

        if (StringUtils.hasText(userUpdateDto.getEmailAddress())) {
            user.setEmailAddress(userUpdateDto.getEmailAddress());
        }

        if (StringUtils.hasText(userUpdateDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }

        UserEntity updatedUser = userRepository.save(user);
        return userResponseAssembler.toModel(updatedUser);
    }

    @Override
    public EntityModel<UserEntityResponse> patchUser(Long id, UserPatchDto userPatchDto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        //only allowed to update if same user or is an admin
        SecurityUtils.checkIfSameUserOrAdmin(existingUser);
        if (StringUtils.hasText(userPatchDto.getFirstName())) {
            existingUser.setFirstName(userPatchDto.getFirstName());
        }

        if (StringUtils.hasText(userPatchDto.getLastName())) {
            existingUser.setLastName(userPatchDto.getLastName());
        }

        if (StringUtils.hasText(userPatchDto.getPhoneNumber())) {
            existingUser.setPhoneNumber(userPatchDto.getPhoneNumber());
        }

        if (StringUtils.hasText(userPatchDto.getEmailAddress())) {
            existingUser.setEmailAddress(userPatchDto.getEmailAddress());
        }

        if (StringUtils.hasText(userPatchDto.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(userPatchDto.getPassword()));
        }

        UserEntity updatedUser = userRepository.save(existingUser);
        return userResponseAssembler.toModel(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
