package com.example.Controler;

import com.example.Model.expense;
import com.example.Model.user;
import com.example.repositories.UserRepository;
import com.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseControler {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    public UserRepository userRepository;



    @PostMapping
    public expense saveExpense(@RequestBody expense expense, @RequestParam long userId) {
        user user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Received Expense: " + expense);
        System.out.println("Received User ID: " + userId);
        return expenseService.saveExpense(expense, userId);
    }
}
