package com.example.controller;

import com.example.model.Income;
import com.example.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incomes")
public class incomeControl {
    @Autowired
    private IncomeService incomeService;
    @PostMapping
    public Income saveincome(@RequestBody Income income, @AuthenticationPrincipal Long userId) {
        return incomeService.saveIncome(income, userId);
    }
}
