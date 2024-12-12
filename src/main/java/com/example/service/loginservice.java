package com.example.service;

import com.example.Model.user;
import com.example.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class loginservice {
    @Autowired
    private LoginRepository loginRepository;

    public user log(String name, String password) {
        user foundUser = loginRepository.findByNameAndPassword(name, password);
        if (foundUser != null) {
            return foundUser;
        } else {
            // Handle user not found
            System.out.println("User not found with provided credentials.");
            return null;
        }
    }
}
