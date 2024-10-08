package com.daytolp.cartapp.backend_cart_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.daytolp.cartapp.backend_cart_app.models.entities.Product;
import com.daytolp.cartapp.backend_cart_app.services.ProductService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public List<Product> list() {
        return service.findAll();
    }
}
