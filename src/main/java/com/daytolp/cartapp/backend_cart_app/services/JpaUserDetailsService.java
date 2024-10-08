package com.daytolp.cartapp.backend_cart_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.daytolp.cartapp.backend_cart_app.constants.Constantes;
import com.daytolp.cartapp.backend_cart_app.repositories.UserRepository;
import java.lang.Boolean;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.daytolp.cartapp.backend_cart_app.models.entities.User user = null;
        Optional<com.daytolp.cartapp.backend_cart_app.models.entities.User> userOpt = userRepository.findByUsername(username);     
        user = userOpt.orElseThrow(() -> new UsernameNotFoundException(Constantes.M0002));
        
        List<GrantedAuthority> authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName()))
        .collect(Collectors.toList());
       
        return new User(user.getUsername(), user.getPassword(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,  Boolean. TRUE, authorities);
    }

}
