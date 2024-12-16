package com.example.service;

import com.example.Model.income;
import com.example.Model.user;
import com.example.repositories.IncomeRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {
    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private UserRepository userRepository;


    public Long getTotalincome(long user_id){
        return incomeRepository.getTotalincomebyId(user_id);
    }

    public List<income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public income saveIncome(income income, long userId) {
        user user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        income.setUser(user);
        return incomeRepository.save(income);
    }
}
