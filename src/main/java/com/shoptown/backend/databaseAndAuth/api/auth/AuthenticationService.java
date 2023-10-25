package com.shoptown.backend.databaseAndAuth.api.auth;

import com.shoptown.backend.databaseAndAuth.api.models.User;
import com.shoptown.backend.databaseAndAuth.api.repo.UserRepository;
import com.shoptown.backend.databaseAndAuth.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Create a UserDetails object instead of the deprecated User class.
        UserDetails userDetails = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Save the UserDetails in the database.
        userRepository.save(userDetails);

        // Generate a JWT token.
        var jwtToken = jwtService.generateToken(userDetails);

        // Return the response.
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate the user using the AuthenticationManager.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Load the UserDetails from the database.
        UserDetails userDetails = userRepository.findByUsername(request.getUsername());

        // Generate a JWT token.
        var jwtToken = jwtService.generateToken(userDetails);

        // Return the response.
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
