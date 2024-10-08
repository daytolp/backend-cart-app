package com.daytolp.cartapp.backend_cart_app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.daytolp.cartapp.backend_cart_app.models.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByname(String name);
}
