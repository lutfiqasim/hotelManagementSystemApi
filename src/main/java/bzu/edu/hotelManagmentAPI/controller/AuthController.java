package bzu.edu.hotelManagmentAPI.controller;

import bzu.edu.hotelManagmentAPI.assembler.AuthResponseAssembler;
import bzu.edu.hotelManagmentAPI.dto.AuthResponseDto;
import bzu.edu.hotelManagmentAPI.dto.LoginDto;
import bzu.edu.hotelManagmentAPI.dto.RegisterDto;
import bzu.edu.hotelManagmentAPI.model.Role;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import bzu.edu.hotelManagmentAPI.repository.RoleRepository;
import bzu.edu.hotelManagmentAPI.repository.UserRepository;
import bzu.edu.hotelManagmentAPI.security.JWTGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLDataException;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    private final AuthResponseAssembler authResponseAssembler;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
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

    @PostMapping("/login")
    public ResponseEntity<EntityModel<AuthResponseDto>> login(@Valid @RequestBody LoginDto loginDto) {
        if (!userRepository.existsByEmailAddress(loginDto.getEmail())) {
            throw new UsernameNotFoundException("Check your email or password");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail()
                        , loginDto.getPassword())
        );
        SecurityContextHolder.getContext().
                setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        UserEntity user = userRepository.findByEmailAddress(loginDto.getEmail()).orElse(null);
        EntityModel<AuthResponseDto> responseDtoEntityModel;

        if (user != null) {
            responseDtoEntityModel = authResponseAssembler.toModel(user);
            responseDtoEntityModel.getContent().setAccessToken(token);
            return new ResponseEntity<>(responseDtoEntityModel, HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        if (userRepository.existsByEmailAddress(registerDto.getEmail())) {
            return new ResponseEntity<>("Email address is already in use", HttpStatus.CONFLICT);
        }
        UserEntity user = new UserEntity();
        user.setEmailAddress(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPhoneNumber(registerDto.getPhoneNo());
        Role roles = roleRepository.findByName("CUSTOMER").get();
        user.setRoles(Collections.singletonList(roles));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Phone number already in use");
        }

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}
