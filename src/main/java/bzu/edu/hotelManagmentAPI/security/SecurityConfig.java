package bzu.edu.hotelManagmentAPI.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig 
// extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>  
{

    private JwtAuthEntryPoint authEntryPoint;
    private CustomUserDetailsService userDetailsService;
    private JWTGenerator jwtGenerator;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthEntryPoint authEntryPoint, JWTGenerator jwtGenerator) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.jwtGenerator = jwtGenerator;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.
                                requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/v1/users/employees").hasRole(UserRole.ADMIN.name())
//                                .requestMatchers("/api/v1/users/**").hasRole("CUSTOMER")
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthenticationFilter(authenticationManager(new AuthenticationConfiguration())), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    // @Override
    // public void configure(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())
    //         .exceptionHandling(exceptionHandling ->
    //                 exceptionHandling.authenticationEntryPoint(authEntryPoint))
    //         .sessionManagement(sessionManagement ->
    //                 sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .authorizeHttpRequests(authorize -> authorize
    //                 .requestMatchers("/api/auth/**" ).permitAll()
    //                 .anyRequest().authenticated())
    //         .httpBasic(Customizer.withDefaults())
    //         .addFilterBefore(jwtAuthenticationFilter(authenticationManager(new AuthenticationConfiguration())), UsernamePasswordAuthenticationFilter.class);
    // }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    @Bean
//    public JWTAuthenticationFilter jwtAuthenticationFilter() {
//        return new JWTAuthenticationFilter(authenticationManager());
//    }
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        return new JWTAuthenticationFilter(jwtGenerator, userDetailsService, authenticationManager);
    }
}
