package com.shoptown.backend.databaseAndAuth.api.controllers;

import com.shoptown.backend.databaseAndAuth.api.models.AccountDeleteRequest;
import com.shoptown.backend.databaseAndAuth.api.models.User;
import com.shoptown.backend.databaseAndAuth.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/delete-account")
public class DeletionController {
    private final MongoTemplate mongoTemplate;
    private final BlackListService blackListService;

    @Autowired
    public DeletionController(MongoTemplate mongoTemplate, BlackListService blackListService) {
        this.mongoTemplate = mongoTemplate;
        this.blackListService = blackListService;
    }

    @PostMapping("")
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal UserDetails userDetails, @RequestBody AccountDeleteRequest request) {
        String username = request.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        mongoTemplate.remove(query, User.class);
        blackListService.addToBlackList(request.getToken());
        return ResponseEntity.ok("User deleted successfully");
    }
}
