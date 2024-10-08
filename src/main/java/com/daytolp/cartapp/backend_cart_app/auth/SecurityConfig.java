package com.daytolp.cartapp.backend_cart_app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.daytolp.cartapp.backend_cart_app.auth.filters.JWTAuthenticationFilter;
import com.daytolp.cartapp.backend_cart_app.auth.filters.JWTAuthorizationFilter;
import com.daytolp.cartapp.backend_cart_app.auth.providers.JwtProvider;

@Configuration
public class SecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtProvider jwtProvider;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request -> 
            request.requestMatchers(HttpMethod.GET, "/users").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
            .requestMatchers("/users/**").hasRole("ADMIN")
            .anyRequest().authenticated())
        .addFilter(new JWTAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), jwtProvider))
        .addFilter(new JWTAuthorizationFilter(authenticationConfiguration.getAuthenticationManager(), jwtProvider))
        .csrf(config -> config.disable())
        .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
