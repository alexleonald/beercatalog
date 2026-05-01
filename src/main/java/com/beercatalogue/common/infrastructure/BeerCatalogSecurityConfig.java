package com.beercatalogue.common.infrastructure;

import com.beercatalogue.common.application.UserSourcePort;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static com.beercatalogue.common.infrastructure.ApiConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class BeerCatalogSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Public
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/metrics", "/actuator/info").permitAll()
                        // GET public
                        .requestMatchers(HttpMethod.GET, MANUFACTURERS+"/**", BEERS+"/**").permitAll()
                        // Manufacturer + admin can create
                        .requestMatchers(HttpMethod.POST, MANUFACTURERS+"/**").hasAnyRole(ROLE_ADMIN, ROLE_MANUFACTURER)
                        // Update → ADMIN + MANUFACTURER
                        .requestMatchers(HttpMethod.PUT, MANUFACTURERS+"/**").hasAnyRole(ROLE_ADMIN, ROLE_MANUFACTURER)
                        // ADMIN has all rights
                        .requestMatchers(MANUFACTURERS+"/**", BEERS+"/**").hasRole(ROLE_ADMIN)
                        // rest
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                }))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserSourcePort userSource,
            PasswordEncoder passwordEncoder
    ) {
        List<UserDetails> users = userSource.getAllUsers().stream()
                .map(u -> User.builder()
                        .username(u.username())
                        .password(passwordEncoder.encode(u.password()))
                        .roles(u.roles().split(","))
                        .build()
                )
                .toList();

        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}