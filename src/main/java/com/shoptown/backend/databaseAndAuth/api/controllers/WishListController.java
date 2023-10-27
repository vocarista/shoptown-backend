package com.shoptown.backend.databaseAndAuth.api.controllers;

import com.shoptown.backend.databaseAndAuth.api.models.CartProduct;
import com.shoptown.backend.databaseAndAuth.api.models.DeleteRequest;
import com.shoptown.backend.databaseAndAuth.api.models.User;
import com.shoptown.backend.databaseAndAuth.api.models.WishlistProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user/wishlist")
public class WishListController {
    private final MongoTemplate mongoTemplate;
    @Autowired
    public WishListController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("")
    public ResponseEntity<List<WishlistProduct>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<WishlistProduct> wishlist = user.getWishlist();

        if (wishlist == null) {
            wishlist = new ArrayList<>();
            Update update = new Update().set("wishlist", wishlist);
            mongoTemplate.updateFirst(query, update, User.class);
        }

        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-to-wishlist")
    public ResponseEntity<String> addItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody WishlistProduct request) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<WishlistProduct> wishlist = user.getWishlist();

        if (wishlist == null) {
            wishlist = new ArrayList<>();
        }

        if (!wishlist.contains(request))
            wishlist.add(request);

        Update update = new Update().set("wishlist", wishlist);
        mongoTemplate.updateFirst(query, update, User.class);
        return ResponseEntity.ok("Product added to wishlist");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DeleteRequest request) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("uername").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<WishlistProduct> wishlist = user.getWishlist();
        wishlist.removeIf(item -> Objects.equals(item.getProductId(), request.getProductId()));
        Update update = new Update().set("wishlist", wishlist);
        mongoTemplate.updateFirst(query, update, User.class);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
