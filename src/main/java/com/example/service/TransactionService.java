package com.example.service;

import com.example.Model.CategoryPercentageDTO;
import com.example.Model.IncomeExpenseDTO;
import com.example.repositories.TotalTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TotalTransactionRepository totalTransactionRepository;

    public IncomeExpenseDTO calculateTotals(long user_id, Integer year, Integer month, Integer date) {
        return totalTransactionRepository.getTotalIncomeAndExpense(user_id, year, month, date);
    }
    public List<CategoryPercentageDTO> getCategoryBreakdown(long userId, Integer year, Integer month, Integer date) {
        return totalTransactionRepository.getCategoryPercentage(userId, year, month, date);
    }
}
