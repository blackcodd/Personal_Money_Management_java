package com.example.model;

public record AuthResponse(String token, Long userId, String name, String email) {
}
