package com.shoptown.backend.databaseAndAuth.api.controllers;

import com.shoptown.backend.databaseAndAuth.api.models.ProductRequest;
import com.shoptown.backend.databaseAndAuth.api.models.User;
import com.shoptown.backend.databaseAndAuth.api.repo.ProductRepository;
import com.shoptown.backend.databaseAndAuth.api.repo.UserRepository;
import com.shoptown.backend.databaseAndAuth.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/orders")
public class OrderController {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private JwtService jwtService;

    @Autowired
    public OrderController(UserRepository userRepository, ProductRepository productRepository, JwtService jwtService) {
        this.jwtService=jwtService;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> getOrders(@RequestBody String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username);
        List<String> orderlist = user.getOrderlist();
        return ResponseEntity.ok(orderlist);
    }

    @PostMapping("/add-to-orders/{productId}")
    public ResponseEntity<?> addItem(@RequestBody ProductRequest request) {
        String username = jwtService.extractUsername(request.getToken());
        User user = userRepository.findByUsername(username);
        List<String> orderlist = user.getOrderlist();
        orderlist.add(request.getProductId());
        userRepository.save(user);
        return ResponseEntity.ok("Product added to orders");
    }
}
