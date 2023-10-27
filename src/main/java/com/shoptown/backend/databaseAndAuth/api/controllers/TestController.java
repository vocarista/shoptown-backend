package com.shoptown.backend.databaseAndAuth.api.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("")
    public ResponseEntity<String> getTestString (@RequestBody TestRequest testRequest) {
        return ResponseEntity.ok("You sent:" + testRequest.getBody());
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class TestRequest {
    private String body;
}
