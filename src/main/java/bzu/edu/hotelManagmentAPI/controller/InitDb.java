package bzu.edu.hotelManagmentAPI.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.Role;
import bzu.edu.hotelManagmentAPI.repository.RoleRepository;
import jakarta.annotation.PostConstruct;

@Configuration
public class InitDb {

    @Bean
    CommandLineRunner initDatabase(RoleRepository repository) {
        return args -> {
            for (UserRole role : UserRole.values()) {
                if (repository.findByName(role) == null) {
                    System.out.println(role.name());
                    Role newRole = new Role();
                    newRole.setName(role);
                    repository.save(newRole);
                }
            }
        };
    }
}