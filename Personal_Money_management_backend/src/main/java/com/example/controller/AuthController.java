package com.example.controller;

import com.example.model.AuthRequest;
import com.example.model.AuthResponse;
import com.example.model.RegisterRequest;
import com.example.model.User;
import com.example.security.JwtService;
import com.example.service.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAuthService userAuthService;
    private final JwtService jwtService;

    public AuthController(UserAuthService userAuthService, JwtService jwtService) {
        this.userAuthService = userAuthService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User registeredUser = userAuthService.register(request.name(), request.email(), request.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(registeredUser));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User authenticatedUser = userAuthService.login(request.name(), request.password());
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid username or password"));
        }
        return ResponseEntity.ok(toResponse(authenticatedUser));
    }

    private AuthResponse toResponse(User authenticatedUser) {
        String token = jwtService.generateToken(authenticatedUser.getId(), authenticatedUser.getName());
        return new AuthResponse(token, authenticatedUser.getId(), authenticatedUser.getName(), authenticatedUser.getEmail());
    }

    private record ErrorResponse(String message) {
    }
}