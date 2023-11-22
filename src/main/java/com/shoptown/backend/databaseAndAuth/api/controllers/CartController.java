package com.shoptown.backend.databaseAndAuth.api.controllers;

import com.shoptown.backend.databaseAndAuth.api.models.CartProduct;
import com.shoptown.backend.databaseAndAuth.api.models.DeleteRequest;
import com.shoptown.backend.databaseAndAuth.api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user/cart")
public class CartController {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public CartController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("")
    public ResponseEntity<List<CartProduct>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<CartProduct> cartlist = user.getCartlist();

        if (cartlist == null) {
            cartlist = new ArrayList<CartProduct>();
            Update update  = new Update().set("cartlist", cartlist);
            mongoTemplate.updateFirst(query, update, User.class);
        }

        return ResponseEntity.ok(cartlist);
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CartProduct request) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<CartProduct> cartlist = user.getCartlist();

        if (cartlist == null) {
            cartlist = new ArrayList<CartProduct>();
            user.setCartlist(cartlist);
        }

        boolean productExists = false;
        for (CartProduct item: cartlist) {
            if (Objects.equals(item.getProductId(), request.getProductId())) {
                productExists = true;
                break;
            }
        }
        if (productExists) {
            cartlist.forEach(item -> {
                if (Objects.equals(item.getProductId(), request.getProductId())) {
                    item.setQty(request.getQty());
                }
            });
        } else {
            cartlist.add(request);
        }

        Update update = new Update().set("cartlist", cartlist);
        mongoTemplate.updateFirst(query, update, User.class);

        return ResponseEntity.ok("Product added to cart");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DeleteRequest request) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<CartProduct> cartlist = user.getCartlist();
        cartlist.removeIf(item -> Objects.equals(item.getProductId(), request.getProductId()));
        Update update = new Update().set("cartlist", cartlist);
        mongoTemplate.updateFirst(query, update, User.class);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @DeleteMapping("/empty")
    public ResponseEntity<String> removeAll(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            Query query = new Query(Criteria.where("username").is(username));
            Update update = new Update().unset("cartlist");
            mongoTemplate.updateFirst(query, update, User.class);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting cart: " + e.getMessage());
        }
    }
}