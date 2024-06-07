package bzu.edu.hotelManagmentAPI.repository;

import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
