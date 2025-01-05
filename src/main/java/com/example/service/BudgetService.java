package com.example.service;

import com.example.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    public void setBudget(Long id, Date start_date,Date end_date,Long amount){
        budgetRepository.insertBudget(id,start_date,end_date,amount);
    }


}
