package com.daytolp.cartapp.backend_cart_app.auth.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import java.util.HashMap;
import java.util.Collection;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.daytolp.cartapp.backend_cart_app.auth.providers.JwtProvider;
import com.daytolp.cartapp.backend_cart_app.constants.Constantes;
import com.daytolp.cartapp.backend_cart_app.models.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtProvider jwtProvider;

    private AuthenticationManager authenticationManager;

    ObjectMapper objectMapper;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        User user = null;
        String username = null, password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Al realizar la authenticacion con el authenticationManager por debajo manda a llamar el metodo loadUserByUsername del UserDetailsService
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();

        // Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(authorities)).build();
        boolean isAdmin = authorities.stream().anyMatch(r -> r.getAuthority().equals(Constantes.ROL_ADMIN));
        jwtProvider.getKey();
        String token = Jwts.builder()
            .claim("authorities", authorities)
            .claim("isAdmin", isAdmin).subject(username)
            // .signWith(Constantes.SECRET_KEY)
            .signWith(jwtProvider.getKey())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000)).compact();

        response.addHeader(Constantes.AUTHORIZATION, Constantes.BEARER + token);

        Map<String, Object> body = new HashMap<>();
        body.put(Constantes.TOKEN, token);
        body.put(Constantes.MESSAGE, Constantes.M0003);
        body.put(Constantes.USERNAME, username);    

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(Constantes.STATUS_HTTP_200);
		response.setContentType(Constantes.APPLICATION_JSON);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
                Map<String, Object> body = new HashMap<>();
                body.put(Constantes.MESSAGE, Constantes.M0004);
                body.put(Constantes.ERROR, failed.getMessage());
                response.getWriter().write(new ObjectMapper().writeValueAsString(body));
                response.setStatus(Constantes.STATUS_HTTP_401);
                response.setContentType(Constantes.APPLICATION_JSON);
    }

}
