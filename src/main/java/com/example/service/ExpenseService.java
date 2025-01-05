package com.example.service;

import com.example.Model.ExpenseDTO;
import com.example.Model.expense;
import com.example.Model.user;
import com.example.repositories.ExpenseRepository;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;
    public Map<String, Double> getpieTotalExpense(long user_id, Integer year, Integer month, Integer date)
    {
        List<Object[]> results;
        if (year != null && month != null && date != null)
        {results=expenseRepository.findCategoryAndPercentageByUserIdAndYearAndMonthAndDate(user_id, year, month, date);}
        else if (year != null && month != null) { results=expenseRepository.findCategoryAndPercentageByUserIdAndYearAndMonth(user_id, year, month); }

        else { results=expenseRepository.findCategoryAndPercentageByUserId(user_id); }
        Map<String, Double> categoryPercentageMap = new HashMap<>();
        for (Object[] result : results)
        { String category = (String) result[0];
            Double percentage = (Double) result[2];
            categoryPercentageMap.put(category, percentage);
        }
        return  categoryPercentageMap;
    }

    public expense saveExpense(expense expense, long userId) {
        user user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Saving Expense: " + expense);
        expense.setUser(user);
        return expenseRepository.save(expense);
    }
    public double calculateTotalExpense(long userId, String startDate, String endDate) {
        // Fetch total expense from the repository
        return expenseRepository.findTotalExpenseByUserAndDateRange(userId, startDate, endDate)
                .orElse(0.0);
    }
}
