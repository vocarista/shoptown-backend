package com.shoptown.backend.databaseAndAuth.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {
    @GetMapping("")
    public ResponseEntity<String> sendPing() {
        return ResponseEntity.ok("Server pinged successfully");
    }
}
