package com.shoptown.backend.databaseAndAuth.api.auth;

import com.shoptown.backend.databaseAndAuth.api.models.User;
import com.shoptown.backend.databaseAndAuth.api.repo.UserRepository;
import com.shoptown.backend.databaseAndAuth.config.JwtService;
import com.shoptown.backend.databaseAndAuth.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final BlackListService blackListService;
    private final MongoTemplate mongoTemplate;

    public AuthenticationResponse register(RegisterRequest request) {
        // Check if a user with the same username already exists.
        if (userRepository.existsByUsername(request.getUsername())) {
            // Authenticate the existing user.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // If authentication is successful, the existing user can create a new user.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // User does not exist, handle registration as usual.
            UserDetails userDetails = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.save(userDetails);
        }

        // Generate a JWT token.
        UserDetails userDetails = (UserDetails) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        ).getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);

        // Return the response.
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate the user using the AuthenticationManager.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Set the authentication in the SecurityContext.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Load the UserDetails from the database.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate a JWT token.
        String jwtToken = jwtService.generateToken(userDetails);

        // Return the response.
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse logout(LogoutRequest request) {
        blackListService.addToBlackList(request.getToken());
        return AuthenticationResponse.builder().token("Logged out successfully").build();
    }

    public Boolean isAvailable(String request) {
        String username = request;
        Query query = new Query(Criteria.where("username").is(username));
        List<User> list= mongoTemplate.find(query, User.class);
        return list.isEmpty();
    }

    public Boolean isValid(String request) {
        String username = jwtService.extractUsername(request);
        Query query = new Query(Criteria.where("username").is(username));
        UserDetails user = mongoTemplate.findOne(query, User.class);
        assert user != null;
        return jwtService.isTokenValid(request, user);
    }
}
