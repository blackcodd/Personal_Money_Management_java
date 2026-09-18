package com.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.model.User;
import com.example.repositories.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void registerAndLoginShouldWorkForValidCredentials() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserAuthService service = new UserAuthService(userRepository, passwordEncoder);

        when(userRepository.existsByName("alice")).thenReturn(false);
        when(userRepository.findByEmail("alice@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findByName("alice")).thenAnswer(invocation -> Optional.of(savedUser(passwordEncoder)));

        User saved = service.register("alice", "alice@example.com", "secret");

        assertNotNull(saved);
        assertEquals("alice", saved.getName());
        assertNotNull(service.login("alice", "secret"));
        assertNull(service.login("alice", "wrong-password"));
    }

    private User savedUser(PasswordEncoder passwordEncoder) {
        User saved = new User();
        saved.setName("alice");
        saved.setEmail("alice@example.com");
        saved.setPassword(passwordEncoder.encode("secret"));
        return saved;
    }
}
