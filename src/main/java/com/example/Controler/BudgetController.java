package com.example.Controler;

import com.example.repositories.BudgetRepository;
import com.example.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;
    @Autowired
    private BudgetRepository budgetRepository;

    @PostMapping("/setbudget")
    public ResponseEntity<String> setBudget(
            @RequestParam("id") Long id,
            @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate, @RequestParam("amount") double amount)
   {
        try {
            budgetService.setBudget(id, startDate, endDate,amount);
            return ResponseEntity.ok("Budget set successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to set budget: " + e.getMessage());
        }
    }
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkBudget(@RequestParam("user_id") Long userId) {
        int  exists = budgetRepository.existId(userId);
        boolean bo=false;
        if(exists>0) bo=true;
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", bo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBudget(@RequestParam("user_id") Long userId) {
        try {
            int rowsDeleted = budgetRepository.deletebudgetByid(userId);
            if (rowsDeleted > 0) {
                return ResponseEntity.ok("Previous budget deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No budget found for the given user ID.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete budget: " + e.getMessage());
        }
    }


}
