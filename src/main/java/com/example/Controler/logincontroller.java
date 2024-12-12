package com.example.Controler;

import com.example.Model.user;
import com.example.service.loginservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
public class logincontroller {
    @Autowired
    private loginservice userservice;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("user", new user());
        return mav;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") user user, Model model) {
        user autonicateUser = userservice.log(user.getName(), user.getPassword());
        System.out.print(autonicateUser);
        if (Objects.nonNull(autonicateUser)) {
            return "redirect:/homepage";
        } else {
             model.addAttribute("message","Wrong username or password");
               return "login";
        }
    }
}
