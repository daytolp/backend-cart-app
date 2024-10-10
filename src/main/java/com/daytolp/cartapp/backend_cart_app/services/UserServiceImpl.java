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
    public List<UserDTO> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users
                .stream()
                .map(u -> {
                    boolean isAdmin = u.getRoles().stream().anyMatch(r -> Constantes.ROL_ADMIN.equals(r.getName()));
                    UserDTO dto = new UserDTO();
                    dto.setId(u.getId());
                    dto.setAdmin(isAdmin);
                    dto.setEmail(u.getEmail());
                    dto.setUsername(u.getUsername());
                    return dto;
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
    public UserDTO save(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByname(Constantes.ROL_USER));
        if (user.isAdmin()) {
            roles.add(roleRepository.findByname(Constantes.ROL_ADMIN));
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSaved = repository.save(user);

        UserDTO dto = new UserDTO();
        dto.setId(userSaved.getId());
        dto.setAdmin(user.isAdmin());
        dto.setEmail(userSaved.getEmail());
        dto.setUsername(userSaved.getUsername());

        return dto;
    }

    @Override
    @Transactional
    public Optional<UserDTO> update(UserDTO user, Long id) {
        Optional<User> o = this.findById(id);

        UserDTO dto = new UserDTO();
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
            User userOptional = repository.save(userDb);

           
            dto.setId(userOptional.getId());
            dto.setAdmin(user.isAdmin());
            dto.setEmail(userOptional.getEmail());
            dto.setUsername(userOptional.getUsername());
        }
        return Optional.ofNullable(dto);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }
    
}