package com.shoptown.backend.databaseAndAuth.service;

import com.shoptown.backend.databaseAndAuth.api.models.Product;
import com.shoptown.backend.databaseAndAuth.api.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    @Autowired
    public ProductService(ProductRepository productRepository, MongoTemplate mongoTemplate){
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> getSearchedProducts(String keyword){
        Criteria titleCrtiteria = Criteria.where("title").regex(".*" + keyword + ".*", "i");
        Query titleQuery = new Query(titleCrtiteria);
        List<Product> titleList = mongoTemplate.find(titleQuery, Product.class);
        Query categoryCriteria = new Query(Criteria.where("category").regex(".*" + keyword + ".*"));
        List<Product> categoryList = mongoTemplate.find(categoryCriteria, Product.class);

        if (keyword.length() <= 5) {
            categoryList.addAll(titleList);
            return  categoryList;
        } else {
            titleList.addAll(categoryList);
            return titleList;
        }
    }
}
