package com.example.service;

import com.example.model.Expense;
import com.example.model.Income;
import com.example.model.User;
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

//
//    public Long getTotalincome(long user_id){
//        return incomeRepository.getTotalincomebyId(user_id);
//    }
//    public Long getTotalIncome(long user_id, Integer year, Integer month, Integer date)
//    { if (year != null && month != null && date != null)
//    { return incomeRepository.findTotalIncomeByUserIdAndYearAndMonthAndDate(user_id, year, month, date); }
//    else if (year != null && month != null) { return incomeRepository.findTotalIncomeByUserIdAndYearAndMonth(user_id, year, month); }
//    else if(year!=null){ return incomeRepository.findTotalIncomeByUserIdAndYear(user_id,year);}
//    else { return incomeRepository.findTotalIncomeByUserId(user_id);}
//    }
//
//    public List<income> allincome(long user_id) { return incomeRepository.allincome(user_id);}
//    public List<income> getAllIncomes() {
//        return incomeRepository.findAll();
//    }
    public Income saveIncome(Income income, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        income.setUser(user);
        return incomeRepository.save(income);
    }
}
