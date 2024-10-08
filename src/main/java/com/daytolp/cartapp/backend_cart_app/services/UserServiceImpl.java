package com.daytolp.cartapp.backend_cart_app.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import com.daytolp.cartapp.backend_cart_app.constants.Constantes;
import com.daytolp.cartapp.backend_cart_app.dtos.UserDTO;
import com.daytolp.cartapp.backend_cart_app.models.entities.Role;
import com.daytolp.cartapp.backend_cart_app.models.entities.User;
import com.daytolp.cartapp.backend_cart_app.repositories.RoleRepository;
import com.daytolp.cartapp.backend_cart_app.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users
                .stream()
                .map(u -> {
                    boolean isAdmin = u.getRoles().stream().anyMatch(r -> Constantes.ROL_ADMIN.equals(r.getName()));
                    u.setAdmin(isAdmin);
                    return u;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByname(Constantes.ROL_USER));
        if (user.isAdmin()) {
            roles.add(roleRepository.findByname(Constantes.ROL_ADMIN));
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> update(UserDTO user, Long id) {
        Optional<User> o = this.findById(id);
        User userOptional = null;
        if (o.isPresent()) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleRepository.findByname(Constantes.ROL_USER));
            if (user.isAdmin()) {
                roles.add(roleRepository.findByname(Constantes.ROL_ADMIN));
            }

            User userDb = o.orElseThrow();
            userDb.setRoles(roles);
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = this.save(userDb);
        }
        return Optional.ofNullable(userOptional);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }
    
}