package com.shoptown.backend.databaseAndAuth.api.controllers;

import com.shoptown.backend.databaseAndAuth.api.models.UpdateRequest;
import com.shoptown.backend.databaseAndAuth.api.models.User;
import org.apache.coyote.Response;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    MongoTemplate mongoTemplate;

    public UserController(MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    PasswordEncoder passwordEncoder;

    @GetMapping("/details")
    public ResponseEntity<User> getUserDetails (@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update/firstname")
    public ResponseEntity<?> updateFirstName(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateRequest newFirstName) {
        return updateField(userDetails, "firstname", newFirstName.getValue());
    }

    @PostMapping("/update/lastname")
    public ResponseEntity<?> updateLastName(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateRequest newLastName) {
        return updateField(userDetails, "lastname", newLastName.getValue());
    }

    @PostMapping("/update/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateRequest newPassword) {
        String newPass = passwordEncoder.encode(newPassword.getValue());
        return updateField(userDetails, "password", newPass);
    }

    @PostMapping("/update/email")
    public ResponseEntity<?> updateEmail(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateRequest newEmail) {
        return updateField(userDetails, "email", newEmail.getValue());
    }

    @PostMapping("/update/phone")
    public ResponseEntity<?> updatePhone(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateRequest newPhone) {
        return updateField(userDetails, "phone", newPhone.getValue());
    }

    @PostMapping("/update/dob")
    public ResponseEntity<?> updateDob(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Date newDob) {
        return updateField(userDetails, "dob", newDob);
    }

    @PostMapping("/update/username")
    public ResponseEntity<?> updateUsername (@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateRequest newUsername) {
        return updateField(userDetails, "username", newUsername.getValue());
    }

    private ResponseEntity<?> updateField(UserDetails userDetails, String fieldName, Object newValue) {
        String username = userDetails.getUsername();
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().set(fieldName, newValue);
        mongoTemplate.updateFirst(query, update, User.class);
        return ResponseEntity.ok("Updated Successfully");
    }

}
