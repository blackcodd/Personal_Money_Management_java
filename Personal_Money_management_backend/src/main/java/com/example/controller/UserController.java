package com.example.controller;

import com.example.model.User;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
//    @GetMapping()
//    public  String getRegPage(Model model){
//        model.addAttribute("user",new user());
//        return "Mlogin";
//    }
    @PostMapping("/registration")
    public  String saveUser(@ModelAttribute("user") User U, Model model){
        try {
            userRepository.save(U);
            model.addAttribute("message","User successfully saved");
            model.addAttribute("messageType","success");
            return "Mlogin";
        }
        catch (Exception e){
            model.addAttribute("message","ERROR"+e.getMessage());
            model.addAttribute("messageType","error");
            return "Mlogin";
        }



    }


}
