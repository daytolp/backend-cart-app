package com.daytolp.cartapp.backend_cart_app.services;

import java.util.List;
import java.util.Optional;

import com.daytolp.cartapp.backend_cart_app.dtos.UserDTO;
import com.daytolp.cartapp.backend_cart_app.models.entities.User;

public interface UserService {
    List<UserDTO> findAll();

    Optional<User> findById(Long id);

    UserDTO save(User user);

    Optional<UserDTO> update(UserDTO user, Long id);

    void remove(Long id);

}
