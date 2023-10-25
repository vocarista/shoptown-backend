package com.shoptown.backend.databaseAndAuth.api.repo;

import com.shoptown.backend.databaseAndAuth.api.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends MongoRepository<UserDetails, String> {
    User findByUsername(String username);
}
