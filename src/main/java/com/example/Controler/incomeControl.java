package com.example.Controler;

import com.example.Model.income;
import com.example.Model.user;
import com.example.repositories.IncomeRepository;
import com.example.repositories.UserRepository;
import com.example.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class incomeControl {
    @Autowired
    private IncomeService incomeService;
    @Autowired
    public UserRepository userRepository;

    @PostMapping
    public income saveincome(@RequestBody income income, @RequestParam long userId) {
        user user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Received Income: " + income);
        System.out.println("Received User ID: " + userId);
        return incomeService.saveIncome(income, userId);
    }
}
