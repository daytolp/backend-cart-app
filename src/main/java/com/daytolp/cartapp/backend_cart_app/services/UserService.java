package com.daytolp.cartapp.backend_cart_app.services;

import java.util.List;
import java.util.Optional;

import com.daytolp.cartapp.backend_cart_app.dtos.UserDTO;
import com.daytolp.cartapp.backend_cart_app.models.entities.User;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    Optional<User> update(UserDTO user, Long id);

    void remove(Long id);

}
