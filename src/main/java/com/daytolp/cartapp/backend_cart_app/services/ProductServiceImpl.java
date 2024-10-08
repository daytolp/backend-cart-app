package com.daytolp.cartapp.backend_cart_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daytolp.cartapp.backend_cart_app.models.entities.Product;
import java.util.List;
import com.daytolp.cartapp.backend_cart_app.repositories.ProductRepository;



@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return (List<Product>) repository.findAll();
    }
}

