package com.shoptown.backend.databaseAndAuth.api.controllers;

import com.shoptown.backend.databaseAndAuth.api.models.CartProduct;
import com.shoptown.backend.databaseAndAuth.api.models.DeleteRequest;
import com.shoptown.backend.databaseAndAuth.api.models.OrderProduct;
import com.shoptown.backend.databaseAndAuth.api.models.User;
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
@RequestMapping("/user/orders")
public class OrderController {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("")
    public ResponseEntity<List<OrderProduct>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<OrderProduct> orderlist = user.getOrderlist();

        if (orderlist == null) {
            orderlist = new ArrayList<OrderProduct>();
            Update update = new Update().set("orderlist", orderlist);
            mongoTemplate.updateFirst(query, update, User.class);
        }
        return ResponseEntity.ok(orderlist);
    }

    @PostMapping("/add-to-orders")
    public ResponseEntity<String> addItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody List<OrderProduct> request) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<OrderProduct> orderlist = user.getOrderlist();

        if (orderlist == null) {
            orderlist = new ArrayList<OrderProduct>();
        }
        orderlist.addAll(request);
        Update update = new Update().set("orderlist", orderlist);
        mongoTemplate.updateFirst(query, update, User.class);

        return ResponseEntity.ok("Product added to orders");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeItem(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DeleteRequest request) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("uername").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<OrderProduct> orderlist = user.getOrderlist();
        orderlist.removeIf(item -> Objects.equals(item.getProductId(), request.getProductId()));
        Update update = new Update().set("order", orderlist);
        mongoTemplate.updateFirst(query, update, User.class);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
