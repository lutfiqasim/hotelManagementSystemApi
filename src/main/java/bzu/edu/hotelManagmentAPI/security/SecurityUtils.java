package bzu.edu.hotelManagmentAPI.security;

import bzu.edu.hotelManagmentAPI.enums.UserRole;
import bzu.edu.hotelManagmentAPI.model.UserEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static void checkIfSameUserOrAdmin(UserEntity user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equals(user.getEmailAddress())) {
            checkIfAdminAuthority();
        }
    }

    public static void checkIfAdminAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !HasAuthority.hasAuthority(auth, UserRole.ADMIN.name())) {
            throw new AuthorizationServiceException("User Unauthorized");
        }
    }
}

