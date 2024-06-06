package bzu.edu.hotelManagmentAPI.security;

import org.springframework.security.core.Authentication;

public class HasAuthority {
    public static boolean hasAuthority(Authentication auth, String authorityName) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authorityName));
    }
}
