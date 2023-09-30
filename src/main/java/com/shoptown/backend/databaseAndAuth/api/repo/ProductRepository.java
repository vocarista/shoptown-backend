package com.shoptown.backend.databaseAndAuth.api.repo;

import com.shoptown.backend.databaseAndAuth.api.models.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {
}
