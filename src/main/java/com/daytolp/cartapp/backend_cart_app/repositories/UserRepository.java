package com.daytolp.cartapp.backend_cart_app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.daytolp.cartapp.backend_cart_app.models.entities.User;
import java.util.Optional;

public interface UserRepository   extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
