package bzu.edu.hotelManagmentAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JWTGenerator {


    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
        //TODO: understand how it get the actual role :)
        String role = authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet()).iterator().next();
        String token = Jwts.builder()
                .subject(email).issuedAt(new Date())
                .expiration(expireDate)
                .claim("role", role)
                .signWith(SecurityConstants.JWT_SECRET)
                .compact();
        return token;
    }

    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .build().
                parseSignedClaims(token)
                .getBody();
        System.out.println("______");
        System.out.println(claims.getSubject());
        System.out.println("______");


        return claims.getSubject();
    }

    //    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).build().parseSignedClaims(token);
//            return true;
//        } catch (JwtException ex) {
//            System.out.println("here1");
//            throw new AuthenticationCredentialsNotFoundException(ex.getMessage());
//        } catch (Exception e) {
//            System.out.println("here22");
//            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
//        }
//    }
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new MalformedJwtException("Failed to decode token", e);
        }
    }

}
