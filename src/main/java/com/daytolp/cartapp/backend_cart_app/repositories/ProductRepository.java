package com.daytolp.cartapp.backend_cart_app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.daytolp.cartapp.backend_cart_app.models.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    
}