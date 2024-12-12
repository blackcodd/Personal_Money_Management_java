package com.example.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homepagecontroller {
    @GetMapping("/homepage")
    public String homepage(){
        return "homepage";
    }
}
