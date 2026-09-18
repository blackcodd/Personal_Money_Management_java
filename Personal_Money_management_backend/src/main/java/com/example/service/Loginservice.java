package com.example.service;

import com.example.model.User;
import com.example.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Loginservice {
    @Autowired
    private LoginRepository loginRepository;

    public User log(String name, String password) {
        User foundUser = loginRepository.findByNameAndPassword(name, password);
        if (foundUser != null) {
            return foundUser;
        }
        System.out.println("User not found with provided credentials.");
        return null;
    }
}
