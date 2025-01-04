package com.example.Controler;

import com.example.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/setbudget")
    public ResponseEntity<String> setBudget(
            @RequestParam("id") Long id,
            @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, @RequestParam("amount") Long amount)
   {
        try {
            budgetService.setBudget(id, startDate, endDate,amount);
            return ResponseEntity.ok("Budget set successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to set budget: " + e.getMessage());
        }
    }
}
