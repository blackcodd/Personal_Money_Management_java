package com.example.controller;

import com.example.model.CategoryPercentageDTO;
import com.example.model.ExpenseDTO;
import com.example.model.IncomeExpenseDTO;
import com.example.model.User;
import com.example.repositories.TransactionRepository;
import com.example.repositories.UserRepository;
import com.example.service.ExpenseService;
import com.example.service.IncomeService;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class homecontroler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    IncomeService incomeService;
    @Autowired
    ExpenseService expenseService;
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/homepage")
    public String getRegPage(@AuthenticationPrincipal Long userId, Model model) {
        try {
            model.addAttribute("user_id", userId);
            transactionRepository.getAllTransactionsByUserId(userId);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        model.addAttribute("user", new User());
        return "homepage";
    }
    @GetMapping("/allTransaction")
    public ResponseEntity<List<ExpenseDTO>> getAllTransactions(@AuthenticationPrincipal Long userId) {
        try {
            List<ExpenseDTO> transactions = transactionRepository.getAllTransactionsByUserId(userId);

            if (transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(transactions);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/filterTransactions")
    public ResponseEntity<List<ExpenseDTO>> filterTransactions(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer date) {

        List<ExpenseDTO> filteredTransactions = transactionRepository.filterTransactions(userId, year, month, date);
        return ResponseEntity.ok(filteredTransactions);
    }


    @Autowired
    private TransactionService transactionService;

    @GetMapping("/totals")
    public ResponseEntity<IncomeExpenseDTO> getTotals(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer date) {
        IncomeExpenseDTO incomeExpenseDTO=transactionService.calculateTotals(userId, year, month, date);
        return ResponseEntity.ok(incomeExpenseDTO);
    }
    @GetMapping("/category-percentages")
    public ResponseEntity<List<CategoryPercentageDTO>> getCategoryPercentages(
            @AuthenticationPrincipal Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer date) {
        List<CategoryPercentageDTO> percentages = transactionService.getCategoryBreakdown(userId, year, month, date);
        return ResponseEntity.ok(percentages);
    }
    @GetMapping("/expenseinperiod")
    public ResponseEntity<Map<String, Double>> getTotalExpense(
            @AuthenticationPrincipal Long userId,
            @RequestParam ("start_date")String start_date,
            @RequestParam ("end_date") String end_date) {
        try {

            // Calculate the total expense
            double totalExpense = expenseService.calculateTotalExpense(userId, start_date, end_date);

            // Prepare response
            Map<String, Double> response = new HashMap<>();
            response.put("totalExpense", totalExpense);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}