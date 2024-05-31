package bzu.edu.hotelManagmentAPI.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private JWTGenerator tokenGenerator;

    private CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JWTAuthenticationFilter(JWTGenerator tokenGenerator, CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager) {
        this.tokenGenerator = tokenGenerator;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, AuthenticationCredentialsNotFoundException {
        String token = getJWTFromRequest(request);
        try {

            if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
                String email = tokenGenerator.getEmailFromJWT(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

    }
//    doesn't work
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
//            , FilterChain filterChain) throws ServletException, IOException {
//        String token = getJWTFromRequest(request);
//        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
//            String email = tokenGenerator.getEmailFromJWT(token);
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
//            System.out.println(userDetails);
////            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
////            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////            System.out.println("AuthenticationToken:");
////            System.out.println(authenticationToken);
////            System.out.println("Context");
////            System.out.println(SecurityContextHolder.getContext());
////            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            //check if good
//            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            System.out.println("AuthenticationToken:");
//            System.out.println(authenticationToken);
//            System.out.println("Context");
//            System.out.println(SecurityContextHolder.getContext());
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
//            System.out.println(authentication);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        filterChain.doFilter(request, response);
//    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String headerToken = request.getHeader("Authorization");
        //After checking Bearer is from OAuth 2.0 authorization
        if (StringUtils.hasText(headerToken) && headerToken.startsWith("Bearer ")) {
            String toReturn = headerToken.substring(7, headerToken.length()).strip();
            return toReturn;
        }
        return null;
    }
}
