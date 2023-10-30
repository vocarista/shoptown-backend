package com.shoptown.backend.databaseAndAuth.api.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping(value = "", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> index() {
        try {
            // Load the index.html file from the classpath
            Resource resource = new ClassPathResource("static/index.html");

            if (resource.exists()) {
                return ResponseEntity.ok(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Handle exceptions here if needed
            return ResponseEntity.status(500).build();
        }
    }
}
