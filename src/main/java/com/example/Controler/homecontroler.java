package com.example.Controler;

import com.example.Model.user;
import com.example.repositories.UserRepository;
import com.example.service.ExpenseService;
import com.example.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class homecontroler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    IncomeService incomeService;
    @Autowired
    ExpenseService expenseService;

    @GetMapping("/homepage")
    public String getRegPage( @RequestParam long user_id, Model model) {
        try {

            Long totalincome = incomeService.getTotalincome(user_id);
            Long totalexpence=expenseService.getTotalexpence(user_id);
            Long saving=totalincome-totalexpence;
            model.addAttribute("totalexpense",totalexpence);
            model.addAttribute("totalincome", totalincome);
            model.addAttribute("savings", saving);

            model.addAttribute("user_id",user_id);
            System.out.println("total="+totalincome);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
       model.addAttribute("user", new user());
        return "homepage";
    }
}