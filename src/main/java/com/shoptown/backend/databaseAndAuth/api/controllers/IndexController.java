package com.shoptown.backend.databaseAndAuth.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
    @GetMapping("/")
    public ResponseEntity<String> serverRunning() {
        String index = "Server running at port 8080.";
        return ResponseEntity.ok(index);
    }
}
