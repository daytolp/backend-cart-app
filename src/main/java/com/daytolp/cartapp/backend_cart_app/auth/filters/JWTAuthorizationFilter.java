package com.daytolp.cartapp.backend_cart_app.auth.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.daytolp.cartapp.backend_cart_app.auth.providers.JwtProvider;
import com.daytolp.cartapp.backend_cart_app.constants.*;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProvider jwtProvider;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(Constantes.AUTHORIZATION);

        if (header == null || !header.startsWith(Constantes.BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(Constantes.BEARER, Constantes.CADENA_VACIA);
  
        try {
            Claims claims = getClaims(token);
            String username = claims.getSubject();
            // Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) claims.get("authorities");

            Collection<? extends GrantedAuthority> authorities = Stream.of(claims.get("authorities")).map(role -> (List<Map<String, String>>) role) 
            .flatMap(role -> role.stream()  
                     .map(r -> r.get("authority"))  
                     .map(SimpleGrantedAuthority::new))  
            .toList();

            UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(autenticacion);//al final se agrega la utenticacion al contexto de spring y con eso se authentica para que pueda obtener el recurso de la peticion solicitada
            chain.doFilter(request, response);
        } catch (JwtException e) {
            e.printStackTrace();
            Map<String, String> body = new HashMap<>();
            body.put(Constantes.ERROR, e.getMessage());
            body.put(Constantes.MESSAGE, Constantes.M0001);
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(Constantes.STATUS_HTTP_401);
            response.setContentType(Constantes.APPLICATION_JSON);
        }        
    }

    public Claims getClaims(String token) {  
        return Jwts.parser()  
                .verifyWith(jwtProvider.getKey()) 
                .build()  
                .parseSignedClaims(token)  
                .getPayload();  
    }  

}
