package com.example.Controler;

import com.example.Model.CategoryPercentageDTO;
import com.example.Model.ExpenseDTO;
import com.example.Model.IncomeExpenseDTO;
import com.example.Model.user;
import com.example.repositories.TransactionRepository;
import com.example.repositories.UserRepository;
import com.example.service.ExpenseService;
import com.example.service.IncomeService;
import com.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String getRegPage( @RequestParam long user_id, Model model) {
        try {
            model.addAttribute("user_id",user_id);

           //  Long totalincome = incomeService.getTotalincome(user_id);
           // Long totalexpence=expenseService.getTotalexpence(user_id);
           // Long saving=totalincome-totalexpence;
           // model.addAttribute("totalexpense",totalexpence);
           // model.addAttribute("totalincome", totalincome);
            //model.addAttribute("savings", saving);
            List<ExpenseDTO>Table_list=transactionRepository.getAllTransactionsByUserId(user_id);
         //    model.addAttribute("Transaction",Table_list);

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


       model.addAttribute("user", new user());
        return "homepage";
    }
    @GetMapping("/allTransaction")
    public ResponseEntity<List<ExpenseDTO>> getAllTransactions(@RequestParam long user_id) {
        try {
            List<ExpenseDTO> transactions = transactionRepository.getAllTransactionsByUserId(user_id);

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
            @RequestParam long user_id,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer date) {

        List<ExpenseDTO> filteredTransactions = transactionRepository.filterTransactions(user_id, year, month, date);
        return ResponseEntity.ok(filteredTransactions);
    }


    @Autowired
    private TransactionService transactionService;

    @GetMapping("/totals")
    public ResponseEntity<IncomeExpenseDTO> getTotals(
            @RequestParam long user_id,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer date) {
        IncomeExpenseDTO incomeExpenseDTO=transactionService.calculateTotals(user_id, year, month, date);
        return ResponseEntity.ok(incomeExpenseDTO);
    }
    @GetMapping("/category-percentages")
    public ResponseEntity<List<CategoryPercentageDTO>> getCategoryPercentages(
            @RequestParam long user_id,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer date) {
        List<CategoryPercentageDTO> percentages = transactionService.getCategoryBreakdown(user_id, year, month, date);
        return ResponseEntity.ok(percentages);
    }


}