package com.example.service;

import com.example.Model.expense;
import com.example.Model.user;
import com.example.repositories.ExpenseRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    public List<expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
    public  Long getTotalexpence(long user_id){
        return expenseRepository.gettotalexpence(user_id);
    }

    public expense saveExpense(expense expense, long userId) {
        user user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Saving Expense: " + expense);
        expense.setUser(user);
        return expenseRepository.save(expense);
    }
}
