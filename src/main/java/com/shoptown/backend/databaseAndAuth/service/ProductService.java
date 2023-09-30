package com.shoptown.backend.databaseAndAuth.service;

import com.shoptown.backend.databaseAndAuth.api.models.Product;
import com.shoptown.backend.databaseAndAuth.api.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
