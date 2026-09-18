package com.example.controller;

import com.example.model.User;
import com.example.service.Loginservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
public class logincontroller {
    @Autowired
    private Loginservice userservice;

    @GetMapping()
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("Mlogin");
        mav.addObject("user", new User());
        return mav;
    }
    @GetMapping("/fuserid")
    public ResponseEntity<Long> logieduser(@RequestParam String name, @RequestParam String password) {
        try {
            User authenticatedUser =  new User();
            authenticatedUser=  userservice.log(name, password);
            if (authenticatedUser != null) {
                return ResponseEntity.ok(authenticatedUser.getId());
            } else {
                System.out.println("User with this id and password not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PostMapping ("/login")
    public String login(@ModelAttribute("user") User user, Model model) {
        User autonicateUser = userservice.log(user.getName(), user.getPassword());

        System.out.print(autonicateUser);
        if (Objects.nonNull(autonicateUser)) {

            model.addAttribute("user_id",autonicateUser.getId());
            return "redirect:/homepage?user_id="+autonicateUser.getId();

        } else {
             model.addAttribute("message","Wrong username or password");
               return "Mlogin";
        }
    }
}
