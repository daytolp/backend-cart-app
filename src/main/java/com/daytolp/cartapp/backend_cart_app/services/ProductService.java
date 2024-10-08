package com.daytolp.cartapp.backend_cart_app.services;

import java.util.List;

import com.daytolp.cartapp.backend_cart_app.models.entities.Product;

public interface ProductService {
    List<Product> findAll();
}
