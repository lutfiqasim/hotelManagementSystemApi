package bzu.edu.hotelManagmentAPI.service.AuthenticationServiceImp;

import bzu.edu.hotelManagmentAPI.assembler.AuthResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.AuthResponseDto;
import bzu.edu.hotelManagmentAPI.dto.LoginDto;
import bzu.edu.hotelManagmentAPI.dto.RegisterDto;
import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.Role;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.repository.RoleRepository;
import bzu.edu.hotelManagmentAPI.repository.UserRepository;
import bzu.edu.hotelManagmentAPI.security.JWTGenerator;
import bzu.edu.hotelManagmentAPI.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationServiceImp implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final AuthResponseAssembler authResponseAssembler;

    @Autowired
    public AuthenticationServiceImp(AuthenticationManager authenticationManager,
                                    UserRepository userRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder,
                                    JWTGenerator jwtGenerator,
                                    AuthResponseAssembler authResponseAssembler) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.authResponseAssembler = authResponseAssembler;
    }

    public EntityModel<AuthResponseDto> authenticateUser(LoginDto loginDto) {
        if (!userRepository.existsByEmailAddress(loginDto.getEmail())) {
            throw new UsernameNotFoundException("Check your email or password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = userRepository.findByEmailAddress(loginDto.getEmail()).orElse(null);

        if (user != null) {
            EntityModel<AuthResponseDto> responseDtoEntityModel = authResponseAssembler.toModel(user);
            responseDtoEntityModel.getContent().setAccessToken(token);
            return responseDtoEntityModel;
        }

        return null;
    }

    public String registerUser(RegisterDto registerDto) {
        if (userRepository.existsByEmailAddress(registerDto.getEmail())) {
            return "Email address is already in use";
        }

        UserEntity user = new UserEntity();
        user.setEmailAddress(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPhoneNumber(registerDto.getPhoneNo());

        Role roles = roleRepository.findByName(UserRole.CUSTOMER).orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singletonList(roles));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Phone number already in use", e);
        }

        return "User registered successfully";
    }
}
