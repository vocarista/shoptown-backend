package com.shoptown.backend.databaseAndAuth.api.auth;

import com.shoptown.backend.databaseAndAuth.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/auth")
public class AuthenticationController {
    private final AuthenticationService service;
    private final BlackListService blService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, BlackListService blService) {
        this.service = authenticationService;
        this.blService = blService;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        return ResponseEntity.ok(service.logout(request));
    }

    @PostMapping("/is-username-available")
    public ResponseEntity<Boolean> isAvailable(@RequestBody UsernameCheckRequest request) {
        return ResponseEntity.ok(service.isAvailable(request));
    }

    @PostMapping("/is-token-valid")
    public ResponseEntity<Boolean> isValid(@RequestBody AuthenticationResponse request) {
        return ResponseEntity.ok(service.isValid(request) && blService.isBlackListed(request.getToken()));
    }
}
