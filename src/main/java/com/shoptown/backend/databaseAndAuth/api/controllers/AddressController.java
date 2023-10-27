package com.shoptown.backend.databaseAndAuth.api.controllers;

import com.shoptown.backend.databaseAndAuth.api.models.Address;
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

@RestController
@RequestMapping("/user/address")
public class AddressController {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AddressController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/shipping-address-list")
    public ResponseEntity<List<Address>> getShippingAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<Address> addressList = user.getShippingList();
        if (addressList == null) {
            addressList = new ArrayList<>();
            Update update = new Update().set("shippingList", addressList);
            mongoTemplate.updateFirst(query, update, User.class);
        }

        return ResponseEntity.ok(addressList);
    }

    @GetMapping("/default-address")
    public ResponseEntity<Address> getDefaultAddress(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<Address> addressList = user.getShippingList();

        if (addressList == null) {
            addressList = new ArrayList<>();
            Update update = new Update().set("shippingList", addressList);
            mongoTemplate.updateFirst(query, update, User.class);
        }

        Address defaultAddress = null;
        for (Address item: addressList) {
            if (item.isDefaultAddress())
                defaultAddress = item;
        }
        return ResponseEntity.ok(defaultAddress);
    }

    @GetMapping("/billing-address")
    public ResponseEntity<Address> getBillingAddress(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        Address billingAddress = user.getBillingAddress();

        return ResponseEntity.ok(billingAddress);

    }

    @PostMapping("/add-shipping-address")
    public ResponseEntity<String> addShippingAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Address request) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        List<Address> shippingList = user.getShippingList();

        if (shippingList == null) {
            shippingList = new ArrayList<>();
        }

        shippingList.add(request);
        Update update = new Update().set("shippingList", shippingList);
        mongoTemplate.updateFirst(query, update, User.class);

        return ResponseEntity.ok("Address added successfully");
    }

    @PostMapping("/add-billing-address")
    public ResponseEntity<String> addBillingAddress(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Address address) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().set("billingAddress", address);
        mongoTemplate.updateFirst(query, update, User.class);
        return ResponseEntity.ok("Address added successfully");
    }
}
