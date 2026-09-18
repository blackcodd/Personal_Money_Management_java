package com.example.controller;

import com.example.model.Expense;
import com.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseControler {
    @Autowired
    private ExpenseService expenseService;
    @PostMapping
    public Expense saveExpense(@RequestBody Expense expense, @AuthenticationPrincipal Long userId) {
        return expenseService.saveExpense(expense, userId);
    }
}
