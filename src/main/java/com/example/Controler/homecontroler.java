package com.example.Controler;

import com.example.Model.user;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class homecontroler {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/homepage")
    public String getRegPage(Model model) {
        model.addAttribute("user", new user());
        return "homepage";
    }
}